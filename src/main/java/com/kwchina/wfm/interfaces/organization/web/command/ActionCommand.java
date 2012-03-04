package com.kwchina.wfm.interfaces.organization.web.command;

public abstract class ActionCommand {
	
	public static String ADD = "Add";
	public static String DELETE = "Delete";
	public static String ID_SEPARATOR = ",";
	
	private String commandType;
	private String ids;
	
	public String getCommandType() {
		return commandType;
	}
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
