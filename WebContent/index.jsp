<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="jira.dashboard.dao.*"%>
<%@page import="jira.dashboard.service.*"%>
<%@page import="jira.dashboard.vo.*"%>
<%@page import="jira.util.paging.*"%>
<%@page import="java.util.*"%>

<link rel="stylesheet" type="text/css" href="style.css" />

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>CUBRID Recent Issue Board</title>
	
	<script type="text/javascript">
		function changePeriod(val) {
			location.href = '?period=' + val;
			//iframe.location.href = "http://www.naver.com";
		}
	</script>
</head>
<body>
	<%
		String period = "7 DAY";
	
		int offset = 0;
		int row_count = 0;
	
		if (request.getParameter("period") != null) {
			period = request.getParameter("period");
		}
		request.setAttribute("period", period);

		RecentWeekIssueService service = new RecentWeekIssueService();
		
		int count = service.rowRWICount(period);
		request.setAttribute("count", count);
		System.out.println("count : " + count);
		
		int etcCount = service.getEtcCount(period);
		request.setAttribute("etcCount", etcCount);
		System.out.println("etcCount : " + etcCount);
		
		int curPageNum = 0;
		int recordSize = 50;
		
		if (count > recordSize) {
			curPageNum = 1;
		}

		/* page 처리 부분 */
		Paging paging = new Paging();
		paging.setTotalCount(count); 		// 전체 레코드 갯수
		paging.setRecordSize(recordSize); 	// 한 화면에 뿌려질 레코드 갯수
		paging.setBlockCount(10); 			// 각 레코드가 표시될 페이지의 갯수 제한 [1 2 3 4 ... 10] 요렇게 하겠단 의미

		/* 현재 페이지의 번호를 확인하는 로직 */
		if (request.getParameter("curPageNum") == null) {
			System.out.println("curPageNum == null : " + curPageNum);
			request.setAttribute("curPageNum", paging.getCurPageNum());
		} else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum").toString());
			System.out.println("request.getParameter(curPageNum).toString() : " + request.getParameter("curPageNum").toString());
			paging.setCurPageNum(curPageNum);
			request.setAttribute("curPageNum", curPageNum);
		}
		System.out.println("curPageNum : " + curPageNum);

		/* paging 에 사용되는 변수들을 request 객체에 저장 */
		request.setAttribute("recordSize", paging.getRecordSize());
		request.setAttribute("startPage", paging.getStartPage());
		request.setAttribute("blockCount", paging.getBlockCount());
		request.setAttribute("pageCount", paging.getPageCount());
		System.out.println("recordSize : " + paging.getRecordSize());
		System.out.println("startPage : " + paging.getStartPage());
		System.out.println("blockCount : " + paging.getBlockCount());
		System.out.println("pageCount : " + paging.getPageCount());
		
		int startRecord = paging.getStartRecord(); // 처음 시작 레코드
		int endRecord = paging.getEndRecord(); // 마지막 레코드
		System.out.println("startRecord : " + paging.getStartRecord());
		System.out.println("endRecord : " + paging.getEndRecord());

		// 마지막 레코드 번호 출력에 대한 로직
		if (curPageNum == 0) {
			endRecord = count; // 현재 페이지가 0이면 마지막 번호를 총 갯수로
			System.out.println("[curPageNum == 0] endRecord : " + endRecord);
		}
		// (현재 페이지의 번호 * 화면에 출력되는 레코드 수) 보다 총 레코드 수가 작으면, 마지막 번호를 총 레코드 수로 출력  
		else if (curPageNum * recordSize > count) {
			endRecord = count;
			System.out.println("[curPageNum * recordSize > count] endRecord : " + endRecord);
		}

		request.setAttribute("startRecord", startRecord);
		request.setAttribute("endRecord", endRecord);
		
		offset = startRecord - 1;
		row_count = endRecord - startRecord + 1;

		/* DB에서 가져오는 레코드들을 저장할 list 생성 */
		List<RecentWeekIssueWithWorkLogVO> techIssueList = service.selectPagingResult(offset, row_count, period);
		request.setAttribute("techIssueList", techIssueList);

		int etc_curPageNum = 0;
		int etc_recordSize = 50;
		
		if (etcCount > etc_recordSize) {
			etc_curPageNum = 1;
		}

		Paging etc_paging = new Paging();
		etc_paging.setTotalCount(etcCount);
		etc_paging.setRecordSize(etc_recordSize);
		etc_paging.setBlockCount(10);

		/* 현재 페이지의 번호를 확인하는 로직 */
		if (request.getParameter("etc_curPageNum") == null) {
			System.out.println("etc_curPageNum == null : " + etc_curPageNum);
			request.setAttribute("etc_curPageNum", etc_paging.getCurPageNum());
		} else {
			etc_curPageNum = Integer.parseInt(request.getParameter("etc_curPageNum").toString());
			System.out.println("request.getParameter(etc_curPageNum).toString() : " + request.getParameter("etc_curPageNum").toString());
			etc_paging.setCurPageNum(etc_curPageNum);
			request.setAttribute("etc_curPageNum", etc_curPageNum);
		}
		System.out.println("etc_curPageNum : " + etc_curPageNum);

		/* paging 에 사용되는 변수들을 request 객체에 저장 */
		request.setAttribute("etc_recordSize", etc_paging.getRecordSize());
		request.setAttribute("etc_startPage", etc_paging.getStartPage());
		request.setAttribute("etc_blockCount", etc_paging.getBlockCount());
		request.setAttribute("etc_pageCount", etc_paging.getPageCount());
		
		System.out.println("etc_recordSize : " + etc_paging.getRecordSize());
		System.out.println("etc_startPage : " + etc_paging.getStartPage());
		System.out.println("etc_blockCount : " + etc_paging.getBlockCount());
		System.out.println("etc_pageCount : " + etc_paging.getPageCount());
		
		int etc_startRecord = etc_paging.getStartRecord(); // 처음 시작 레코드
		int etc_endRecord = etc_paging.getEndRecord(); // 마지막 레코드
		
		System.out.println("etc_startRecord : " + etc_paging.getStartRecord());
		System.out.println("etc_endRecord : " + etc_paging.getEndRecord());

		// 마지막 레코드 번호 출력에 대한 로직
		if (etc_curPageNum == 0) {
			etc_endRecord = etcCount; // 현재 페이지가 0이면 마지막 번호를 총 갯수로
			System.out.println("[etc_curPageNum == 0] etc_endRecord : " + etc_endRecord);
		}
		// (현재 페이지의 번호 * 화면에 출력되는 레코드 수) 보다 총 레코드 수가 작으면, 마지막 번호를 총 레코드 수로 출력  
		else if (etc_curPageNum * etc_recordSize > etcCount) {
			etc_endRecord = etcCount;
			System.out.println("[etc_curPageNum * etc_recordSize > etcCount] etc_endRecord : " + etc_endRecord);
		}

		request.setAttribute("etc_startRecord", etc_startRecord);
		request.setAttribute("etc_endRecord", etc_endRecord);

		/* CUBRID 를 제외한 나머지 업무에 대한 내용을 저장할 list */
		
		offset = etc_startRecord - 1;
		row_count = etc_endRecord - etc_startRecord + 1;
		
		List<RecentWeekIssueWithCommentVO> rndIssueList = service.selectEtcResult(offset, row_count, period);
		request.setAttribute("rndIssueList", rndIssueList);
	%>

	<div class="default-m-p default-align main-title">
		<span>기술본부 / 개발본부 / QA본부 - 최근 이슈 현황판</span>
	</div>

	<div class="default-m-p sel-period">
		<span>기간 : </span>
		<select onchange="changePeriod(this.value)"
			style="width: 80px">
			<option>선택</option>
			<option value="7 DAY">7일</option>
			<option value="14 DAY">14일</option>
			<option value="21 DAY">21일</option>
			<option value="1 MONTH">1개월</option>
			<option value="2 MONTH">2개월</option>
			<option value="3 MONTH">3개월</option>
			<option value="6 MONTH">6개월</option>
			<option value="1 YEAR">1년</option>
		</select>
	</div>

	<div class="default-m-p default-align">
		<div class="main-contents-title">
			<span>기술본부</span>
		</div>
		
		<table>
			<thead>
				<tr>
					<th class="jira-issuetype" rowspan="2">분류</th>
					<th class="jira-summary" rowspan="2">Summary</th>
					<th class="jira-worklog-full" colspan="3">Work Log</th>
					<th class="jira-assignee" rowspan="2">담당자</th>
					<th class="jira-reporter" rowspan="2">보고자</th>
					<th class="jira-issuestatus" rowspan="2">상태</th>
					<th class="jira-resolution" rowspan="2">해결</th>
					<th class="jira-created" rowspan="2">생성일</th>
					<th class="jira-updated" rowspan="2">수정일</th>
					<th class="jira-duedate" rowspan="2">만기일</th>
				</tr>
				<tr>
					<th class="jira-worklog-author">작성자</th>
					<th class="jira-worklog-starthate">최근날짜</th>
					<th class="jira-worklog-timeworked">총시간</th>
				</tr>
			</thead>

			<tbody>
				<c_rt:forEach var="techIssue" items="${techIssueList}">
					<c_rt:choose>
						<c_rt:when test="${techIssue.issuestatus == 'Resolved'}">
							<tr id="alignCenter" style="background-color: #e0ffe0;">
						</c_rt:when>
						<c_rt:otherwise>
							<c_rt:choose>
								<c_rt:when test="${techIssue.issuestatus == 'Closed'}">
									<tr id="alignCenter" style="background-color: #e0ffe0;">
								</c_rt:when>
								<c_rt:otherwise>
									<tr id="alignCenter">
								</c_rt:otherwise>
							</c_rt:choose>
						</c_rt:otherwise>
					</c_rt:choose>
					<td>${techIssue.issuetype}</td>
					<td style="text-align: left; padding-left: 8px">
						<a href="http://dev.cubrid.com:8888/browse/${techIssue.pkey}" target="_blank">${techIssue.summary}</a>
					</td>
					<td>${techIssue.author}</td>
					<td>${techIssue.startdate}</td>
					<td style="text-align: right; padding-right: 4px;">${techIssue.timeworked}</td>
					<td>${techIssue.assignee}</td>
					<td>${techIssue.reporter}</td>
					<td>${techIssue.issuestatus}</td>
					<td>${techIssue.resolution}</td>
					<td>${techIssue.created}</td>
					<td>${techIssue.updated}</td>
					<td>${techIssue.duedate}</td>
					</tr>
				</c_rt:forEach>
			</tbody>

			<tfoot>
				<tr>
					<td colspan="9">Displaying issues ${startRecord} to ${endRecord} of <b>${count}</b> matching issues.</td>
					<td colspan="3">
						<c_rt:if test="${curPageNum > blockCount}">
							<a href="?curPageNum=${startPage - 1}&period=${period}">◀</a>
						</c_rt:if>
						
						<c_rt:forEach var="num" begin="${startPage}" end="${startPage + blockCount - 1}">
							<c_rt:choose>
								<c_rt:when test="${num > pageCount}">
								</c_rt:when>
								<c_rt:otherwise>
									<c_rt:choose>
										<c_rt:when test="${num == curPageNum }">
											<b>${num}</b>
										</c_rt:when>
										<c_rt:otherwise>
											<a href="?curPageNum=${num}&period=${period}">${num}</a>
										</c_rt:otherwise>
									</c_rt:choose>
								</c_rt:otherwise>
							</c_rt:choose>
						</c_rt:forEach>
						
						<c_rt:if test="${(pageCount - startPage) >= blockCount}">
							<a href="?curPageNum=${startPage + 10}&period=${period}">▶</a>
						</c_rt:if>
					</td>
				</tr>
			</tfoot>
		</table>
	</div>

	<div class="default-m-p default-align">
		<div class="main-contents-title">
			<span>개발본부 / QA본부</span>
		</div>
		
		<table>
			<thead>
				<tr>
					<th class="jira-issuetype" rowspan="2">분류</th>
					<th class="jira-summary" rowspan="2">Summary</th>
					<th class="jira-comment-full" colspan="3">Comment</th>
					<th class="jira-assignee" rowspan="2">담당자</th>
					<th class="jira-reporter" rowspan="2">보고자</th>
					<th class="jira-issuestatus" rowspan="2">상태</th>
					<th class="jira-resolution" rowspan="2">해결</th>
					<th class="jira-created" rowspan="2">생성일</th>
					<th class="jira-updated" rowspan="2">수정일</th>
					<th class="jira-duedate" rowspan="2">만기일</th>
				</tr>
				<tr>
					<th class="jira-comment-author">작성자</th>
					<th class="jira-comment-updated" colspan="2">수정일</th>
				</tr>
			</thead>

			<tbody>
				<c_rt:forEach var="rndIssue" items="${rndIssueList}">
					<c_rt:choose>
						<c_rt:when test="${rndIssue.issuestatus == 'Resolved'}">
							<tr id="alignCenter" style="background-color: #e0ffe0;">
						</c_rt:when>
						<c_rt:otherwise>
							<c_rt:choose>
								<c_rt:when test="${rndIssue.issuestatus == 'Closed'}">
									<tr id="alignCenter" style="background-color: #e0ffe0;">
								</c_rt:when>
								<c_rt:otherwise>
									<tr id="alignCenter">
								</c_rt:otherwise>
							</c_rt:choose>
						</c_rt:otherwise>
					</c_rt:choose>
					<td>${rndIssue.issuetype}</td>
					<td style="text-align: left; padding-left: 8px">
						<a href="http://jira.cubrid.com:8888/browse/${rndIssue.pkey}" target="_blank">${rndIssue.summary}</a>
					</td>
					<td>${rndIssue.comment_author}</td>
					<td colspan="2">${rndIssue.comment_updated}</td>
					<td>${rndIssue.assignee}</td>
					<td>${rndIssue.reporter}</td>
					<td>${rndIssue.issuestatus}</td>
					<td>${rndIssue.resolution}</td>
					<td>${rndIssue.created}</td>
					<td>${rndIssue.updated}</td>
					<td>${rndIssue.duedate}</td>
					</tr>
				</c_rt:forEach>
			</tbody>

			<tfoot>
				<tr>
					<td colspan="9">Displaying issues ${etc_startRecord} to ${etc_endRecord} of <b>${etcCount}</b> matching issues.</td>
					<td colspan="3">
						<c_rt:if test="${etc_curPageNum > etc_blockCount}">
							<a href="?etc_curPageNum=${etc_startPage - 1}&period=${period}">◀</a>
						</c_rt:if>
						
						<c_rt:forEach var="etc_num" begin="${etc_startPage}" end="${etc_startPage + etc_blockCount - 1}">
							<c_rt:choose>
								<c_rt:when test="${etc_num > etc_pageCount}">
								</c_rt:when>
								<c_rt:otherwise>
									<c_rt:choose>
										<c_rt:when test="${etc_num == etc_curPageNum }">
											<b>${etc_num}</b>
										</c_rt:when>
										<c_rt:otherwise>
											<a href="?etc_curPageNum=${etc_num}&period=${period}">${etc_num}</a>
										</c_rt:otherwise>
									</c_rt:choose>
								</c_rt:otherwise>
							</c_rt:choose>
						</c_rt:forEach>
						
						<c_rt:if test="${(etc_pageCount - etc_startPage) >= etc_blockCount}">
							<a href="?etc_curPageNum=${etc_startPage + 10}&period=${period}">▶</a>
						</c_rt:if>
					</td>
				</tr>
			</tfoot>
		</table>
	</div>
</body>
</html>
