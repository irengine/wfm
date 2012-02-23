package com.kwchina.wfm.interfaces.organization.web.command;

public abstract class ActionCommand {
	
	public static String ADD = "Add";
	public static String DELETE = "Delete";
	
	private String commandType;
	
	public String getCommandType() {
		return commandType;
	}
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}
}
