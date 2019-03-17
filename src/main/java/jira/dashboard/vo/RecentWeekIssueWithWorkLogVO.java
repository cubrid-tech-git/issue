package jira.dashboard.vo;

/**
 * 
 * @author HUN
 * 
 *         recent week issue 와 worklog의 데이터도 같이 저장하기 위해 만든 VO 클래스
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
