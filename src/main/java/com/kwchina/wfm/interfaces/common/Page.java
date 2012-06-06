package com.kwchina.wfm.interfaces.common;

import java.util.List;

public class Page {
	
	private int page;
	private int totalPages;
	private int total;
	@SuppressWarnings("rawtypes")
	private List rows;
	
	@SuppressWarnings("rawtypes")
	public Page(int page, int totalPages, int total, List rows) {
		this.page = page;
		this.totalPages = totalPages;
		this.total = total;
		this.rows = rows;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}
	
	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}
}
