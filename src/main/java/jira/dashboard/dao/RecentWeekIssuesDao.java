package jira.dashboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jira.dashboard.vo.RecentWeekIssueWithCommentVO;
import jira.dashboard.vo.RecentWeekIssueWithWorkLogVO;

/**
 * 
 * @author HUN
 * 
 * Recent Week Issue page에서 사용될 Dao class
 *
 */
public class RecentWeekIssuesDao {
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private List<RecentWeekIssueWithWorkLogVO> list;
	private List<RecentWeekIssueWithCommentVO> commentList;

	/**
	 * @param period
	 * @return int
	 * 
	 *         www.cubrid.com:8888(CUBRID, (구)RND, OFFICE) 에서 최근에 변경된 이슈의 개수를 가져옵니다.
	 */
	public int rowRWICount(String period) {
		int result = 0;
		
		try {
			Connection conn = MyConnectionManager.getTechJiraConnection();
			
			Statement stmt = conn.createStatement();
			
            StringBuilder sql = new StringBuilder();
            sql.append("/* rowRWICount */                                                     ".trim() + " ");
            sql.append("SELECT COUNT(*)                                                       ".trim() + " ");
            sql.append("FROM jiraissue j                                                      ".trim() + " ");
            sql.append("WHERE DATE (updated) >= DATE (subdate(now(), INTERVAL " + period +")) ".trim() + " ");
            
            ResultSet rs = stmt.executeQuery(sql.toString());
			
			if(rs.next() && rs.last()) {
				result = rs.getInt(1);
			}
			
			System.out.println("rowRWICount(" + period + ") : " + result);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * @param startRecord
	 * @param endRecord
	 * @param period
	 * @return
	 * 
	 * 		www.cubrid.com:8888(CUBRID, (구)RND, OFFICE) 에서 최근에 변경된 이슈 목록을 페이징 해서 가져옵니다.
	 * 
	 */
	public List<RecentWeekIssueWithWorkLogVO> selectPagingResult(int offset, int row_count, String period) {
		list = new ArrayList<RecentWeekIssueWithWorkLogVO>();
		
		try {
			Connection conn = MyConnectionManager.getTechJiraConnection();
			
			StringBuilder sql = new StringBuilder();
            sql.append("/* selectPagingResult */                                                                                        ".trim() + " ");
            sql.append("SELECT it.pname AS PNAME                                                                                        ".trim() + " ");
            sql.append("    ,ji.pkey AS PKEY                                                                                            ".trim() + " ");
            sql.append("    ,ji.summary AS SUMMARY                                                                                      ".trim() + " ");
            sql.append("    ,(SELECT cu.display_name FROM cwd_user cu WHERE cu.user_name = ji.assignee LIMIT 1) AS ASSIGNEE             ".trim() + " ");
            sql.append("    ,(SELECT cu.display_name FROM cwd_user cu WHERE cu.user_name = ji.reporter LIMIT 1) AS REPORTER             ".trim() + " ");
            sql.append("    ,(SELECT ist.pname FROM issuestatus ist WHERE ist.id = ji.issuestatus LIMIT 1) AS ISSUESTATUS               ".trim() + " ");
            sql.append("    ,(SELECT rs.pname FROM resolution rs WHERE rs.id = ji.resolution LIMIT 1) AS RESOLUTION                     ".trim() + " ");
            sql.append("    ,DATE_FORMAT(ji.created, '%y-%m-%d') AS CREATED                                                             ".trim() + " ");
            sql.append("    ,DATE_FORMAT(ji.updated, '%y-%m-%d') AS UPDATED                                                             ".trim() + " ");
            sql.append("    ,DATE_FORMAT(ji.duedate, '%y-%m-%d') AS DUEDATE                                                             ".trim() + " ");
            sql.append("    ,(SELECT cu.display_name FROM cwd_user cu WHERE cu.user_name = T1.worklog_author LIMIT 1) AS WORKLOG_AUTHOR ".trim() + " ");
            sql.append("    ,DATE_FORMAT(T1.worklog_startdate, '%y-%m-%d</br>%H:%i') AS WORKLOG_STARTDATE                               ".trim() + " ");
            sql.append("    ,T1.worklog_timeworked AS WORKLOG_TIMEWORKED                                                                ".trim() + " ");
            sql.append("FROM jiraissue ji                                                                                               ".trim() + " ");
            sql.append("    LEFT OUTER JOIN issuetype it ON it.id = ji.issuetype                                                        ".trim() + " ");
            sql.append("    LEFT OUTER JOIN (                                                                                           ".trim() + " ");
            sql.append("        SELECT wl.issueid                                                                                       ".trim() + " ");
            sql.append("            ,max(wl.author) AS WORKLOG_AUTHOR                                                                   ".trim() + " ");
            sql.append("            ,max(wl.startdate) AS WORKLOG_startdate                                                             ".trim() + " ");
            sql.append("            ,sum(wl.timeworked) AS WORKLOG_timeworked                                                           ".trim() + " ");
            sql.append("        FROM worklog wl                                                                                         ".trim() + " ");
            sql.append("        WHERE DATE(wl.startdate) >= DATE(subdate(now(), INTERVAL " + period + "))                               ".trim() + " ");
            sql.append("        GROUP BY wl.issueid                                                                                     ".trim() + " ");
            sql.append("    ) T1 ON ji.id = T1.issueid                                                                                  ".trim() + " ");
            sql.append("WHERE DATE(ji.updated) >= DATE(subdate(now(), INTERVAL " + period + "))                                         ".trim() + " ");
            sql.append("ORDER BY UPDATED DESC, WORKLOG_AUTHOR DESC                                                                      ".trim() + " ");
            sql.append("LIMIT ?, ?                                                                                                      ".trim() + " ");
            
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());		
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, row_count);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RecentWeekIssueWithWorkLogVO vo = new RecentWeekIssueWithWorkLogVO();
				vo.setIssuetype(rs.getString("PNAME"));
				vo.setPkey(rs.getString("PKEY"));
				vo.setSummary(rs.getString("SUMMARY"));
				vo.setAssignee(rs.getString("ASSIGNEE"));
				vo.setReporter(rs.getString("REPORTER"));
				vo.setIssuestatus(rs.getString("ISSUESTATUS"));
				vo.setResolution(rs.getString("RESOLUTION"));
				vo.setCreated(rs.getString("CREATED"));
				vo.setUpdated(rs.getString("UPDATED"));
				vo.setDuedate(rs.getString("DUEDATE"));
				vo.setAuthor(rs.getString("WORKLOG_AUTHOR"));
				vo.setStartdate(rs.getString("WORKLOG_STARTDATE"));
				vo.setTimeworked(worklogToHumanReadable(rs.getInt("WORKLOG_TIMEWORKED")));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * @param period
	 * @return
	 * 
	 * 		jira.cubrid.com:8888(RND) 에서 최근에 변경된 이슈의 개수를 가져옵니다.
	 */
	public int getEtcCount(String period) {
		int result = 0;

		try {
			Connection conn = MyConnectionManager.getRndJiraConnection();
			
			Statement stmt = conn.createStatement();
			
            StringBuilder sql = new StringBuilder();
            sql.append("/* getEtcCount */                                                     ".trim() + " ");
            sql.append("SELECT COUNT(*)                                                       ".trim() + " ");
            sql.append("FROM jiraissue j                                                      ".trim() + " ");
            sql.append("WHERE DATE (updated) >= DATE (subdate(now(), INTERVAL " + period +")) ".trim() + " ");
            
            ResultSet rs = stmt.executeQuery(sql.toString());
			
			if(rs.next() && rs.last()) {
				result = rs.getInt(1);
			}
			
			System.out.println("getEtcCount(" + period + ") : " + result);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * @param startRecord
	 * @param endRecord
	 * @param period
	 * @return
	 * 
	 * 		jira.cubrid.com:8888(RND) 에서 최근에 변경된 이슈 목록을 페이징 해서 가져옵니다.
	 * 
	 */
	public List<RecentWeekIssueWithCommentVO> selectEtcResult(int offset, int row_count, String period) {
		commentList = new ArrayList<RecentWeekIssueWithCommentVO>();
		
		try {
			Connection conn = MyConnectionManager.getRndJiraConnection();
			
            StringBuilder sql = new StringBuilder();
            sql.append("/* selectEtcResult */                                                                                           ".trim() + " ");
            sql.append("SELECT it.pname AS PNAME                                                                                        ".trim() + " ");
            sql.append("    ,CONCAT(pj.pkey, '-', ji.issuenum) AS PKEY                                                                  ".trim() + " ");
            sql.append("    ,ji.summary AS SUMMARY                                                                                      ".trim() + " ");
            sql.append("    ,(SELECT cu.display_name FROM cwd_user cu WHERE cu.user_name = ji.assignee LIMIT 1) AS ASSIGNEE             ".trim() + " ");
            sql.append("    ,(SELECT cu.display_name FROM cwd_user cu WHERE cu.user_name = ji.reporter LIMIT 1) AS REPORTER             ".trim() + " ");
            sql.append("    ,(SELECT ist.pname FROM issuestatus ist WHERE ist.id = ji.issuestatus LIMIT 1) AS ISSUESTATUS               ".trim() + " ");
            sql.append("    ,(SELECT rs.pname FROM resolution rs WHERE rs.id = ji.resolution LIMIT 1) AS RESOLUTION                     ".trim() + " ");
            sql.append("    ,DATE_FORMAT(ji.created, '%y-%m-%d') AS CREATED                                                             ".trim() + " ");
            sql.append("    ,DATE_FORMAT(ji.updated, '%y-%m-%d') AS UPDATED                                                             ".trim() + " ");
            sql.append("    ,DATE_FORMAT(ji.duedate, '%y-%m-%d') AS DUEDATE                                                             ".trim() + " ");
            sql.append("    ,(SELECT cu.display_name FROM cwd_user cu WHERE cu.user_name = T1.comment_author LIMIT 1) AS COMMENT_AUTHOR ".trim() + " ");
            sql.append("    ,DATE_FORMAT(T1.comment_updated, '%y-%m-%d %T') AS COMMENT_UPDATED                                          ".trim() + " ");
            sql.append("FROM jiraissue ji                                                                                               ".trim() + " ");
            sql.append("    INNER JOIN project pj ON pj.id = ji.project                                                                 ".trim() + " ");
            sql.append("    LEFT OUTER JOIN issuetype it ON it.id = ji.issuetype                                                        ".trim() + " ");
            sql.append("    LEFT OUTER JOIN (                                                                                           ".trim() + " ");
            sql.append("        SELECT ja.issueid                                                                                       ".trim() + " ");
            sql.append("            ,max(ja.author) AS comment_author                                                                   ".trim() + " ");
            sql.append("            ,max(ja.updated) AS comment_updated                                                                 ".trim() + " ");
            sql.append("        FROM jiraaction ja                                                                                      ".trim() + " ");
            sql.append("        WHERE DATE(ja.updated) >= DATE(subdate(now(), INTERVAL " + period + "))                                 ".trim() + " ");
            sql.append("        GROUP BY ja.issueid                                                                                     ".trim() + " ");
            sql.append("    ) T1 ON ji.id = T1.issueid                                                                                  ".trim() + " ");
            sql.append("WHERE DATE(ji.updated) >= DATE(subdate(now(), INTERVAL " + period + "))                                         ".trim() + " ");
            sql.append("ORDER BY UPDATED DESC, COMMENT_AUTHOR DESC                                                                      ".trim() + " ");
            sql.append("LIMIT ?, ?                                                                                                      ".trim() + " ");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());        
            pstmt.setInt(1, offset);
            pstmt.setInt(2, row_count);
            
            ResultSet rs = pstmt.executeQuery();
            
			while(rs.next()) {
				RecentWeekIssueWithCommentVO vo = new RecentWeekIssueWithCommentVO();
				vo.setIssuetype(rs.getString("PNAME"));
				vo.setPkey(rs.getString("PKEY"));
				vo.setSummary(rs.getString("SUMMARY"));
				vo.setAssignee(rs.getString("ASSIGNEE"));
				vo.setReporter(rs.getString("REPORTER"));
				vo.setIssuestatus(rs.getString("ISSUESTATUS"));
				vo.setResolution(rs.getString("RESOLUTION"));
				vo.setCreated(rs.getString("CREATED"));
				vo.setUpdated(rs.getString("UPDATED"));
				vo.setDuedate(rs.getString("DUEDATE"));
				vo.setComment_author(rs.getString("COMMENT_AUTHOR"));
				vo.setComment_updated(rs.getString("COMMENT_UPDATED"));
				commentList.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return commentList;
	}
	
	public static String worklogToHumanReadable(int worklogTimeworked) {
		final int MIN = 60; 
		final int HOUR = MIN * 60;
		
		int rmainWorklogTimeworked = worklogTimeworked;
		int h = 0;
		int m = 0;
		
		StringBuilder result = new StringBuilder();
		
		h = rmainWorklogTimeworked / HOUR;
		rmainWorklogTimeworked = (rmainWorklogTimeworked - (h * HOUR));
		if (h != 0) {
			result.append(h + "시간");
		}
		
		m = rmainWorklogTimeworked / MIN;
		rmainWorklogTimeworked = (rmainWorklogTimeworked - (m * MIN));
		if (m != 0) {
			if (result.length() != 0) {
				result.append(" ");
			}
			
			result.append(m + "분");
		}
		
		return result.toString();
	}
}
