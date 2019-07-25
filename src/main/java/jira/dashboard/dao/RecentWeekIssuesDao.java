package jira.dashboard.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jira.dashboard.vo.RecentWeekIssueWithCommentVO;
import jira.dashboard.vo.RecentWeekIssueWithWorkLogVO;

public class RecentWeekIssuesDao {
	/**
	 * '기술지원팀' 프로젝트 카테고리 중에서 최근에 변경된 이슈 개수 
	 */
	public int rowRWICount(String period) {
		int result = 0;
		
		try {
			String sql = "" +
				" SELECT " +
				"     /* rowRWICount */ " +
				"     COUNT(*) " +
				" FROM " +
				"     jiraissue j " +
				" WHERE " +
				"     DATE(j.updated) >= DATE(subdate(now(), interval " + period + ")) " +
				"     AND j.project IN ( " +
				"         SELECT " +
				"             p.id " +
				"         FROM " +
				"             project p " +
				"             INNER JOIN nodeassociation n ON p.id = n.source_node_id " +
				"         WHERE " +
				"             n.sink_node_entity = 'ProjectCategory' " +
				"             AND n.sink_node_id = (SELECT id FROM projectcategory WHERE cname = '기술지원팀') " +
				"     ) ";
			
			PreparedStatement pstmt = MyConnectionManager.getRndTsJiraConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				result = rs.getInt(1);
			}
			
			System.out.println("rowRWICount(" + period + ") : " + result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * '기술지원팀' 프로젝트 카테고리 중에서 최근에 변경된 이슈 목록 
	 */
	public List<RecentWeekIssueWithWorkLogVO> selectPagingResult(int offset, int row_count, String period) {
		List<RecentWeekIssueWithWorkLogVO> list = new ArrayList<RecentWeekIssueWithWorkLogVO>();
		
		try {
			String sql = "" +
				" SELECT " +
				"     /* selectPagingResult */ " +
				"     i.pname AS PNAME, " +
				"     CONCAT(p.pkey, '-', j.issuenum) AS PKEY, " +
				"     j.summary AS SUMMARY, " +
				"     (SELECT display_name FROM cwd_user WHERE user_name = j.assignee) AS ASSIGNEE, " +
				"     (SELECT display_name FROM cwd_user WHERE user_name = j.reporter) AS REPORTER, " +
				"     (SELECT pname FROM issuestatus WHERE id = j.issuestatus) AS ISSUESTATUS, " +
				"     (SELECT pname FROM resolution WHERE id = j.resolution) AS RESOLUTION, " +
				"     DATE_FORMAT(j.created, '%y-%m-%d') AS CREATED, " +
				"     DATE_FORMAT(j.updated, '%y-%m-%d') AS UPDATED, " +
				"     DATE_FORMAT(j.duedate, '%y-%m-%d') AS DUEDATE, " +
				"     (SELECT display_name FROM cwd_user WHERE user_name = w.worklog_author) AS WORKLOG_AUTHOR, " +
				"     DATE_FORMAT(w.worklog_startdate, '%y-%m-%d</br>%H:%i') AS WORKLOG_STARTDATE, " +
				"     w.worklog_timeworked AS WORKLOG_TIMEWORKED " +
				" FROM " +
				"     jiraissue j " +
				"     INNER JOIN project p ON p.id = j.project " +
				"     LEFT OUTER JOIN issuetype i ON i.id = j.issuetype " +
				"     LEFT OUTER JOIN ( " +
				"         SELECT " +
				"             issueid, " +
				"             max(author) AS WORKLOG_AUTHOR, " +
				"             max(startdate) AS WORKLOG_startdate, " +
				"             sum(timeworked) AS WORKLOG_timeworked " +
				"         FROM " +
				"             worklog " +
				"         WHERE " +
				"             DATE(startdate) >= DATE(subdate(now(), INTERVAL " + period + ")) " +
				"         GROUP BY " +
				"             issueid " +
				"     ) w ON j.id = w.issueid " +
				" WHERE " +
				"     DATE(j.updated) >= DATE(subdate(now(), INTERVAL " + period + ")) " +
				"     AND j.project IN ( " +
				"         SELECT " +
				"             p.id " +
				"         FROM " +
				"             project p " +
				"             INNER JOIN nodeassociation n ON p.id = n.source_node_id " +
				"         WHERE " +
				"             n.sink_node_entity = 'ProjectCategory' " +
				"             AND n.sink_node_id = (SELECT id FROM projectcategory WHERE cname = '기술지원팀') " +
				"     ) " +
				" ORDER BY " +
				"     UPDATED DESC, " +
				"     WORKLOG_AUTHOR DESC " +
				" LIMIT ?, ? ";
			
			PreparedStatement pstmt = MyConnectionManager.getRndTsJiraConnection().prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, row_count);
            ResultSet rs = pstmt.executeQuery();
            
			while (rs.next()) {
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
	 * '연구소' 프로젝트 카테고리 중에서 최근에 변경된 이슈 개수
	 */
	public int getEtcCount(String period) {
		int result = 0;
		
		try {
			String sql = "" +
				" SELECT " +
				"     /* getEtcCount */ " +
				"     COUNT(*) " +
				" FROM " +
				"     jiraissue j " +
				" WHERE " +
				"     DATE(j.updated) >= DATE(subdate(now(), interval " + period + ")) " +
				"     AND j.project IN ( " +
				"         SELECT " +
				"             p.id " +
				"         FROM " +
				"             project p " +
				"             INNER JOIN nodeassociation n ON p.id = n.source_node_id " +
				"         WHERE " +
				"             n.sink_node_entity = 'ProjectCategory' " +
				"             AND n.sink_node_id = (SELECT id FROM projectcategory WHERE cname = '연구소') " +
				"     ) ";
			
			PreparedStatement pstmt = MyConnectionManager.getRndTsJiraConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				result = rs.getInt(1);
			}
			
			System.out.println("getEtcCount(" + period + ") : " + result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * '연구소' 프로젝트 카테고리 중에서 최근에 변경된 이슈 목록 
	 */
	public List<RecentWeekIssueWithCommentVO> selectEtcResult(int offset, int row_count, String period) {
		List<RecentWeekIssueWithCommentVO> list = new ArrayList<RecentWeekIssueWithCommentVO>();
		
		try {
			String sql = "" +
				" SELECT " +
				"     /* selectEtcResult */ " +
				"     i.pname AS PNAME, " +
				"     CONCAT(p.pkey, '-', j.issuenum) AS PKEY, " +
				"     j.summary AS SUMMARY, " +
				"     (SELECT display_name FROM cwd_user WHERE user_name = j.assignee) AS ASSIGNEE, " +
				"     (SELECT display_name FROM cwd_user WHERE user_name = j.reporter) AS REPORTER, " +
				"     (SELECT pname FROM issuestatus WHERE id = j.issuestatus) AS ISSUESTATUS, " +
				"     (SELECT pname FROM resolution WHERE id = j.resolution) AS RESOLUTION, " +
				"     DATE_FORMAT(j.created, '%y-%m-%d') AS CREATED, " +
				"     DATE_FORMAT(j.updated, '%y-%m-%d') AS UPDATED, " +
				"     DATE_FORMAT(j.duedate, '%y-%m-%d') AS DUEDATE, " +
				"     (SELECT display_name FROM cwd_user WHERE user_name = a.comment_author) AS COMMENT_AUTHOR, " +
				"     DATE_FORMAT(a.comment_updated, '%y-%m-%d %T') AS COMMENT_UPDATED " +
				" FROM jiraissue j " +
				"     INNER JOIN project p ON p.id = j.project " +
				"     LEFT OUTER JOIN issuetype i ON i.id = j.issuetype " +
				"     LEFT OUTER JOIN ( " +
				"         SELECT " +
				"             issueid, " +
				"             max(author) AS comment_author, " +
				"             max(updated) AS comment_updated " +
				"         FROM " +
				"             jiraaction " +
				"         WHERE " +
				"             DATE(updated) >= DATE(subdate(now(), INTERVAL " + period +")) " +
				"         GROUP BY " +
				"             issueid " +
				"     ) a ON j.id = a.issueid " +
				" WHERE " +
				"     DATE(j.updated) >= DATE(subdate(now(), INTERVAL " + period +")) " +
				"     AND j.project IN ( " +
				"         SELECT " +
				"             p.id " +
				"         FROM " +
				"             project p " +
				"             INNER JOIN nodeassociation n ON p.id = n.source_node_id " +
				"         WHERE " +
				"             n.sink_node_entity = 'ProjectCategory' " +
				"             AND n.sink_node_id = (SELECT id FROM projectcategory WHERE cname = '연구소') " +
				"     ) " +
				" ORDER BY " +
				"     UPDATED DESC, " +
				"     COMMENT_AUTHOR DESC " +
				" LIMIT ?, ? ";
			
			PreparedStatement pstmt = MyConnectionManager.getRndTsJiraConnection().prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, row_count);
            ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
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
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
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
