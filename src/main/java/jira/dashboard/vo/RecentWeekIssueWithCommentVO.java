package jira.dashboard.vo;

/**
 * 
 * @author HUN
 * 
 *         recent week issue 와 worklog의 데이터도 같이 저장하기 위해 만든 VO 클래스
 */
public class RecentWeekIssueWithCommentVO extends RecentWeekIssueVO {
	private String comment_author;
	private String comment_created;
	private String comment_updated;
	private String issueid;
	private String worklogbody;

	public String getComment_author() {
		return comment_author;
	}

	public void setComment_author(String comment_author) {
		this.comment_author = comment_author;
	}

	public String getComment_created() {
		return comment_created;
	}

	public void setComment_created(String comment_created) {
		this.comment_created = comment_created;
	}

	public String getComment_updated() {
		return comment_updated;
	}

	public void setComment_updated(String comment_updated) {
		this.comment_updated = comment_updated;
	}

	public String getIssueid() {
		return issueid;
	}

	public void setIssueid(String issueid) {
		this.issueid = issueid;
	}

	public String getWorklogbody() {
		return worklogbody;
	}

	public void setWorklogbody(String worklogbody) {
		this.worklogbody = worklogbody;
	}
}
