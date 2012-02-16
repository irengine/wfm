package com.kwchina.wfm.domain.model.organization;

import javax.persistence.Embeddable;

@Embeddable
public class Preference {
	
	private String key;
	
	private String value;
	
	private String scope;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
