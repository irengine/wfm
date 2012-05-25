package com.kwchina.wfm.interfaces.report;

import com.kwchina.wfm.domain.model.employee.TimeSheet;

public class EmployeeInfo {
	private Long employeeId;
	private String employeeCode;
	private String employeeName;
	
	private Long unitId;
	private String unitName;
	private String unitUriName;
	
	public EmployeeInfo(TimeSheet ts) {
		this.employeeId = ts.getEmployee().getId();
		this.employeeCode = ts.getEmployee().getEmployeeId().getId();
		this.employeeName = ts.getEmployee().getName();
		
		this.unitId = ts.getUnit().getId();
		this.unitName = ts.getUnit().getName();
		this.unitUriName = ts.getUnit().getUriName();
	}
	
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitUriName() {
		return unitUriName;
	}
	public void setUnitUriName(String unitUriName) {
		this.unitUriName = unitUriName;
	}
	
	
}
