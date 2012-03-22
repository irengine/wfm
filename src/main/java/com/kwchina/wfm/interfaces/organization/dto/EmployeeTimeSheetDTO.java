package com.kwchina.wfm.interfaces.organization.dto;

import java.util.TreeMap;

public class EmployeeTimeSheetDTO {

	private Long id;
	private String name;
	private TreeMap<String, String> values;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TreeMap<String, String> getValues() {
		return values;
	}
	public void setValues(TreeMap<String, String> values) {
		this.values = values;
	}
}
