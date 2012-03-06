package com.kwchina.wfm.interfaces.organization.web.command;

import com.kwchina.wfm.domain.model.employee.TimeSheet;

public class QueryTimeSheetCommand {

	private Long unitId;
	private String date;
	private TimeSheet.ActionType actionType;
	
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
	public TimeSheet.ActionType getActionType() {
		return actionType;
	}
	public void setActionType(TimeSheet.ActionType actionType) {
		this.actionType = actionType;
	}

}
