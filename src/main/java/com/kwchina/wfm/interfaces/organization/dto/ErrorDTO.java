package com.kwchina.wfm.interfaces.organization.dto;

public class ErrorDTO {

	private String message;
	
	public ErrorDTO() {
		
	}
	
	public ErrorDTO(String message) {
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
