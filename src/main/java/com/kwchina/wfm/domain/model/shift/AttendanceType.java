package com.kwchina.wfm.domain.model.shift;

public class AttendanceType {
	
	private String name;
	private int beginHour;
	private int endHour;
	
	public AttendanceType(String name, int beginHour, int endHour) {
		this.name = name;
		this.beginHour = beginHour;
		this.endHour = endHour;
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
