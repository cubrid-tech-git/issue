package jira.util.paging;

public class Paging {
	private int totalRowCount; // 전체 레코드 수 : DB에서 가져옴
	private int pageColCount; // 한 페이지에 뿌려질 레코드의 수 : 초기화 필요
	private int blockCount; // 한 블록당 뿌려질 페이지번호의 갯수 : 초기화 필요
	private int totalPageCount; // 총 페이지 수
	private int startRecord;// 페이지번호에 해당하는 레코드의 시작점
	private int endRecord; // 페이지번호에 해당하는 레코드의 끝점
	private int curPageNum = 1; // 현재 페이지 번호
	private int temp; // 시작 페이지를 구하기 위한 임시 변수
	private int startPage; // 시작 페이지

	public void setRecordSize(int recordSize) {
		this.pageColCount = recordSize;
	}

	public int getRecordSize() {
		return pageColCount;
	}

	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}

	public int getBlockCount() {
		return blockCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalRowCount = totalCount;
	}

	public int getTotalCount() {
		return totalRowCount;
	}

	public int getPageCount() {
		totalPageCount = (int) Math.ceil((double) this.totalRowCount / this.pageColCount);
		return totalPageCount;
	}

	public int getStartRecord() {
		startRecord = (curPageNum - 1) * this.getRecordSize() + 1;
		return startRecord;
	}

	public int getEndRecord() {
		endRecord = this.getStartRecord() - 1 + this.getRecordSize();
		return endRecord;
	}

	public int getCurPageNum() {
		curPageNum = this.getEndRecord() / this.getRecordSize();
		return curPageNum;
	}

	public void setCurPageNum(int curPageNum) {
		this.curPageNum = curPageNum;
	}

	public int getStartPage() {
		temp = (curPageNum - 1) % blockCount;
		startPage = curPageNum - temp;
		return startPage;
	}

}
