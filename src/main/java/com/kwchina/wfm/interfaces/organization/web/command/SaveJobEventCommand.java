package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class SaveJobEventCommand {

	public static String ID_SEPARATOR = ",";

	private String ids;
	private Long unitId;
	@DateTimeFormat(iso=ISO.DATE)
	private Date effectDate;
	
	public SaveJobEventCommand() {
		
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
	
	
}
