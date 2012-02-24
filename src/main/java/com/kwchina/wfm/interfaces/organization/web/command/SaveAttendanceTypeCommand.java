package com.kwchina.wfm.interfaces.organization.web.command;


public class SaveAttendanceTypeCommand {

	private Long Id;
	private String name;
	private int beginHour;
	private int endHour;;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBeginHour() {
		return beginHour;
	}
	public void setBeginHour(int beginHour) {
		this.beginHour = beginHour;
	}
	public int getEndHour() {
		return endHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	
}
