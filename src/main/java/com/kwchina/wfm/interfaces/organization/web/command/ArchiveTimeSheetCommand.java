package com.kwchina.wfm.interfaces.organization.web.command;

public class ArchiveTimeSheetCommand {

	private String beginTime;
	private String endTime;
	
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
