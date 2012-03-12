package com.kwchina.wfm.domain.model.organization;

import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.kwchina.wfm.domain.common.ValueObject;

@Embeddable
public class Preference implements ValueObject<Preference> {
	
	private static final long serialVersionUID = 7093966149398348885L;

	private String key;
	
	private String value;
	
	private String scope;
	
	public Preference() {
		this.scope = "";
	}
	
	public Preference(String key, String value) {
		this.key = key;
		this.value = value;
		this.scope = "";
	}

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
	
	@Override
	public boolean sameValueAs(final Preference other) {
		return other != null
				&& new EqualsBuilder().append(this.scope, other.scope)
						.append(this.key, other.key)
						.append(this.value, other.value).isEquals();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final Preference other = (Preference) o;

		return sameValueAs(other);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(scope).append(key).append(value)
				.toHashCode();
	}
	  
}
