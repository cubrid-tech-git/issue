package jira.dashboard.vo;

/**
 * 
 * @author HUN
 * 
 *         VO for JIRAISSUE table
 */

public class RecentWeekIssueVO {
	private int id;
	private String pkey;
	private int project;
	private String reporter;
	private String assignee;
	private String issuetype;
	private String summary;
	private String description;
	private String environment;
	private String priority;
	private String resolution;
	private String issuestatus;
	private String created;
	private String updated;
	private String duedate;
	private String resolutiondate;
	private int votes;
	private int watches;
	private int timeoriginalestimate;
	private int timeestimate;
	private int timespent;
	private int workflow_id;
	private int security;
	private int fixfor;
	private int component;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public int getProject() {
		return project;
	}

	public void setProject(int project) {
		this.project = project;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getIssuetype() {
		return issuetype;
	}

	public void setIssuetype(String issuetype) {
		this.issuetype = issuetype;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getIssuestatus() {
		return issuestatus;
	}

	public void setIssuestatus(String issuestatus) {
		this.issuestatus = issuestatus;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getDuedate() {
		return duedate;
	}

	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

	public String getResolutiondate() {
		return resolutiondate;
	}

	public void setResolutiondate(String resolutiondate) {
		this.resolutiondate = resolutiondate;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public int getWatches() {
		return watches;
	}

	public void setWatches(int watches) {
		this.watches = watches;
	}

	public int getTimeoriginalestimate() {
		return timeoriginalestimate;
	}

	public void setTimeoriginalestimate(int timeoriginalestimate) {
		this.timeoriginalestimate = timeoriginalestimate;
	}

	public int getTimeestimate() {
		return timeestimate;
	}

	public void setTimeestimate(int timeestimate) {
		this.timeestimate = timeestimate;
	}

	public int getTimespent() {
		return timespent;
	}

	public void setTimespent(int timespent) {
		this.timespent = timespent;
	}

	public int getWorkflow_id() {
		return workflow_id;
	}

	public void setWorkflow_id(int workflow_id) {
		this.workflow_id = workflow_id;
	}

	public int getSecurity() {
		return security;
	}

	public void setSecurity(int security) {
		this.security = security;
	}

	public int getFixfor() {
		return fixfor;
	}

	public void setFixfor(int fixfor) {
		this.fixfor = fixfor;
	}

	public int getComponent() {
		return component;
	}

	public void setComponent(int component) {
		this.component = component;
	}
}
