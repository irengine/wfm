package com.kwchina.wfm.interfaces.organization.web.command;

public class SaveShiftTypeCommand extends ActionCommand {

	private Long Id;
	private String name;
	private int displayIndex = 0;
	private String displayName;
	private String strategyClassName;
	private String strategyClassParameters;

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
	public int getDisplayIndex() {
		return displayIndex;
	}
	public void setDisplayIndex(int displayIndex) {
		this.displayIndex = displayIndex;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getStrategyClassName() {
		return strategyClassName;
	}
	public void setStrategyClassName(String strategyClassName) {
		this.strategyClassName = strategyClassName;
	}
	public String getStrategyClassParameters() {
		return strategyClassParameters;
	}
	public void setStrategyClassParameters(String strategyClassParameters) {
		this.strategyClassParameters = strategyClassParameters;
	}
	
}
