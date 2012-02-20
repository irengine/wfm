package com.kwchina.wfm.interfaces.organization.web.command;

public class QueryCommand {
	
	private String unitId;
	private String page;
	private String rows;
	private String sidx;
	private String sord;
	private String search;
	private String filters;
	
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getFilters() {
		return filters;
	}
	public void setFilters(String filters) {
		this.filters = filters;
	}
	
	@Override
	public String toString() {
	    final StringBuilder builder = new StringBuilder("\n--- Query command ---\n")
	    	.append("Unit: ").append(this.getUnitId()).append("\n")
	    	.append("Page: ").append(this.getPage()).append("\n")
	    	.append("Rows: ").append(this.getRows()).append("\n")
	    	.append("Index: ").append(this.getSidx()).append("\n")
	    	.append("Sort: ").append(this.getSord()).append("\n")
	    	.append("Search: ").append(this.getSearch()).append("\n")
	    	.append("Filters: ").append(this.getFilters()).append("\n");

	    return builder.toString();
	}
}
