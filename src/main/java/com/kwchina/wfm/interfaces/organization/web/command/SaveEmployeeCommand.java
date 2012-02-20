package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class SaveEmployeeCommand {

	private Long id;
	private String employeeId;
	private String name;
	@DateTimeFormat(iso=ISO.DATE)
	private Date birthday;
	@DateTimeFormat(iso=ISO.DATE)
	private Date beginDateOfWork;
	@DateTimeFormat(iso=ISO.DATE)
	private Date beginDateOfJob;
	private Long unitId;
	
	public SaveEmployeeCommand() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getBeginDateOfWork() {
		return beginDateOfWork;
	}
	public void setBeginDateOfWork(Date beginDateOfWork) {
		this.beginDateOfWork = beginDateOfWork;
	}
	public Date getBeginDateOfJob() {
		return beginDateOfJob;
	}
	public void setBeginDateOfJob(Date beginDateOfJob) {
		this.beginDateOfJob = beginDateOfJob;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
}
