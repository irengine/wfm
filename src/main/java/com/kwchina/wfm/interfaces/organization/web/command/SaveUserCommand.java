package com.kwchina.wfm.interfaces.organization.web.command;

public class SaveUserCommand extends ActionCommand {

	private Long id;
	private String code;
	private String name;
	private String password;
	private String email;
	private String unitIds;
	
	public SaveUserCommand() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUnitIds() {
		return unitIds;
	}
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
}
