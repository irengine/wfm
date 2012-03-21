package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.Map;

public class SaveUnitCommand extends ActionCommand {

	private Long id;
	private Long parentUnitId;
	private String Name;
	private Long shiftTypeId;
	private Map<String, String> properties;
	
	public SaveUnitCommand() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentUnitId() {
		return parentUnitId;
	}
	public void setParentUnitId(Long parentId) {
		this.parentUnitId = parentId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Long getShiftTypeId() {
		return shiftTypeId;
	}
	public void setShiftTypeId(Long shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
}
