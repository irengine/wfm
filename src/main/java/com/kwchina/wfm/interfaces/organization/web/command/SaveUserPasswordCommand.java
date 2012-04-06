package com.kwchina.wfm.interfaces.organization.web.command;


public class SaveUserPasswordCommand {

	private Long id;
	private String password;
	
	public SaveUserPasswordCommand() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
