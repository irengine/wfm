package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.ArrayList;
import java.util.List;

import com.kwchina.wfm.domain.model.employee.TimeSheet;

public class QueryTimeSheetCommand extends QueryCommand {

//	private Long unitId;
	private String date;
	private TimeSheet.ActionType actionType;
	private String unitIds;
	private Long employeeId;
	
//	public Long getUnitId() {
//		return unitId;
//	}
//	public void setUnitId(Long unitId) {
//		this.unitId = unitId;
//	}
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
	public String getUnitIds() {
		return unitIds;
	}
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public List<Long> getUnitIdList() {
		List<Long> idList = new ArrayList<Long>(); 

		if (unitIds != null) {
			String[] idArray = unitIds.split(",");
			for(String unitId : idArray) {
				idList.add(Long.parseLong(unitId));
			}
		}
		
		return idList;
	}

}
