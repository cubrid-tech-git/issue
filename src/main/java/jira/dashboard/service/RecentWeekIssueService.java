package jira.dashboard.service;

import java.util.List;

import jira.dashboard.dao.RecentWeekIssuesDao;
import jira.dashboard.vo.RecentWeekIssueWithCommentVO;
import jira.dashboard.vo.RecentWeekIssueWithWorkLogVO;

public class RecentWeekIssueService {
	RecentWeekIssuesDao dao = new RecentWeekIssuesDao();

	public int rowRWICount(String period) {
		return dao.rowRWICount(period);
	}

	public List<RecentWeekIssueWithWorkLogVO> selectPagingResult(int offset, int row_count, String period) {
		return dao.selectPagingResult(offset, row_count, period);
	}
	
	public int getEtcCount(String period) {
		return dao.getEtcCount(period);
	}
	
	public List<RecentWeekIssueWithCommentVO> selectEtcResult(int offset, int row_count, String period) {
		return dao.selectEtcResult(offset, row_count, period);
	}

}
