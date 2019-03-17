package jira.dashboard.vo;

/**
 * 
 * @author HUN
 * 
 *         recent week issue �� worklog�� �����͵� ���� �����ϱ� ���� ���� VO Ŭ����
 */
public class RecentWeekIssueWithWorkLogVO extends RecentWeekIssueVO {

	private String author;
	private String startdate;
	private String timeworked;
	private String issueid;
	private String worklogbody;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getTimeworked() {
		return timeworked;
	}

	public void setTimeworked(String timeworked) {
		this.timeworked = timeworked;
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
