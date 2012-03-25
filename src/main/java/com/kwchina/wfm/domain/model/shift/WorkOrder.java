package com.kwchina.wfm.domain.model.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_WORK_ORDER")
public class WorkOrder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column
	private String date;
	
	@Column
	private String employeeCode;
	
	@Column
	private String ShiftCode;
	
	public WorkOrder() {
		
	}
	
	public WorkOrder(String date, String employeeCode, String shiftCode) {
		this.date = date;
		this.employeeCode = employeeCode;
		this.ShiftCode = shiftCode;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getShiftCode() {
		return ShiftCode;
	}
	public void setShiftCode(String shiftCode) {
		ShiftCode = shiftCode;
	}
}
