package com.kwchina.wfm.interfaces.organization.dto;

import java.util.Date;
import java.util.List;

import com.kwchina.wfm.domain.model.employee.TimeSheet;

public class TimeSheetDTO {

	private List<Date> days;
//	private List<TimeSheetRecordDTO> records;
	private List<TimeSheet> records;
	
//	public class TimeSheetRecordDTO {
//		private Employee employee;
//		private Map<String, AttendanceType> dayAttendances;
//		
//		public Employee getEmployee() {
//			return employee;
//		}
//		public void setEmployee(Employee employee) {
//			this.employee = employee;
//		}
//		public Map<String, AttendanceType> getDayAttendances() {
//			return dayAttendances;
//		}
//		public void setDayAttendances(Map<String, AttendanceType> dayAttendances) {
//			this.dayAttendances = dayAttendances;
//		}
//	}

	public List<Date> getDays() {
		return days;
	}

	public void setDays(List<Date> days) {
		this.days = days;
	}

//	public List<TimeSheetRecordDTO> getRecords() {
//		return records;
//	}
//
//	public void setRecords(List<TimeSheetRecordDTO> records) {
//		this.records = records;
//	}
	
	public List<TimeSheet> getRecords() {
		return records;
	}

	public void setRecords(List<TimeSheet> records) {
		this.records = records;
	}
}
