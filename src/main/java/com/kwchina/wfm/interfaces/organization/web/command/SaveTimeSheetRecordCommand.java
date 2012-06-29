package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.kwchina.wfm.domain.model.employee.TimeSheet.ActionType;

public class SaveTimeSheetRecordCommand extends ActionCommand {

	private Long id;
	private Long unitId;
	private Long employeeId;
	@DateTimeFormat(iso=ISO.DATE)
	private Date date;
	private int beginTime;
	private int endTime;
	private String attendanceTypeName;
	private ActionType actionType;
	
	private Date reportDate;
	private ActionType reportActionType;
	
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
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
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public ActionType getReportActionType() {
		return reportActionType;
	}
	public void setReportActionType(ActionType reportActionType) {
		this.reportActionType = reportActionType;
	}
}
