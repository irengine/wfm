package com.kwchina.wfm.interfaces.organization.web.command;

public class QueryTimeSheetCommand {

	private Long unitId;
	private String date;
	
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
