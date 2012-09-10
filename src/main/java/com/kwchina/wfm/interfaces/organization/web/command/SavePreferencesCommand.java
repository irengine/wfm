package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.List;

public class SavePreferencesCommand {

	private List<SavePreferenceCommand> commands;

	public List<SavePreferenceCommand> getCommands() {
		return commands;
	}

	public void setCommands(List<SavePreferenceCommand> commands) {
		this.commands = commands;
	}
	
}
