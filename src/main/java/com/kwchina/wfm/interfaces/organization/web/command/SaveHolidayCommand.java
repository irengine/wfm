package com.kwchina.wfm.interfaces.organization.web.command;

public class SaveHolidayCommand {

	private String type;
	private String holiday;
	private String dayChangedBefore;
	private String dayChangedAfter;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHoliday() {
		return holiday;
	}
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
	public String getDayChangedBefore() {
		return dayChangedBefore;
	}
	public void setDayChangedBefore(String dayChangedBefore) {
		this.dayChangedBefore = dayChangedBefore;
	}
	public String getDayChangedAfter() {
		return dayChangedAfter;
	}
	public void setDayChangedAfter(String dayChangedAfter) {
		this.dayChangedAfter = dayChangedAfter;
	}
	
}
