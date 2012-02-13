package com.kwchina.wfm.interfaces.common;

public class PageHelper {

	private int currentPage;
	private int pageSize;
	private int pagesCount;
	private int rowsCount;
	private int start;
	
	public PageHelper(int rowsCount, int pageSize) {
		this.rowsCount = rowsCount;
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		doPaging();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void doPaging() {
		this.pagesCount = (int) Math.ceil(( this.rowsCount + this.pageSize - 1) / this.pageSize);

        if (this.currentPage <= 0 || this.currentPage > this.pagesCount) {
            this.currentPage = 1;
        }
        
        this.setStart((this.currentPage - 1) * this.pageSize);
	}
}
