package jira.util.paging;

public class Paging {
	private int totalRowCount; // ��ü ���ڵ� �� : DB���� ������
	private int pageColCount; // �� �������� �ѷ��� ���ڵ��� �� : �ʱ�ȭ �ʿ�
	private int blockCount; // �� ��ϴ� �ѷ��� ��������ȣ�� ���� : �ʱ�ȭ �ʿ�
	private int totalPageCount; // �� ������ ��
	private int startRecord;// ��������ȣ�� �ش��ϴ� ���ڵ��� ������
	private int endRecord; // ��������ȣ�� �ش��ϴ� ���ڵ��� ����
	private int curPageNum = 1; // ���� ������ ��ȣ
	private int temp; // ���� �������� ���ϱ� ���� �ӽ� ����
	private int startPage; // ���� ������

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
