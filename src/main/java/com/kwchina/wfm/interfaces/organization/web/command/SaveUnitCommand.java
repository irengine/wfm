package com.kwchina.wfm.interfaces.organization.web.command;

public class SaveUnitCommand {

	private Long id;
	private Long parentUnitId;
	private String Name;
	
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
	
}