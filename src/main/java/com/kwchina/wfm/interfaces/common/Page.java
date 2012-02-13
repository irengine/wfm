package com.kwchina.wfm.interfaces.common;

import java.util.List;

public class Page {
	
	private int page;
	private int total;
	private int records;
	@SuppressWarnings("rawtypes")
	private List rows;
	
	@SuppressWarnings("rawtypes")
	public Page(int page, int total, int records, List rows) {
		this.page = page;
		this.total = total;
		this.records = records;
		this.rows = rows;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getRecords() {
		return records;
	}
	
	public void setRecords(int records) {
		this.records = records;
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
