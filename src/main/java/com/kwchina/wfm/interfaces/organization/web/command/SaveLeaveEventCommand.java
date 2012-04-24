package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.kwchina.wfm.domain.model.employee.TimeSheet.ActionType;
import com.kwchina.wfm.infrastructure.common.DateHelper;

public class SaveLeaveEventCommand extends ActionCommand {

	private Long id;
	private Long unitId;
	private Long employeeId;
	@DateTimeFormat(iso=ISO.DATE)
	private Date beginDate;
	@DateTimeFormat(iso=ISO.DATE)
	private Date endDate;
	private int days;
	private String attendanceTypeName;
	private ActionType actionType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		if (null == endDate)
			endDate = DateHelper.addDay(beginDate, days - 1);
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getAttendanceTypeName() {
		return attendanceTypeName;
	}
	public void setAttendanceTypeName(String attendanceTypeName) {
		this.attendanceTypeName = attendanceTypeName;
	}
	public ActionType getActionType() {
		return actionType;
	}
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
}
