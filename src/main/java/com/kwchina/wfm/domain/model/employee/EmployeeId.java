package com.kwchina.wfm.domain.model.employee;

import javax.persistence.Embeddable;

import org.apache.commons.lang.Validate;

import com.kwchina.wfm.domain.common.ValueObject;

@Embeddable
public class EmployeeId implements ValueObject<EmployeeId> {

	private static final long serialVersionUID = 6177556244823039067L;

	private String id;
	
	public EmployeeId() {
		
	}
	
	public EmployeeId(String id) {
		Validate.notNull(id);
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		EmployeeId other = (EmployeeId) o;

		return sameValueAs(other);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean sameValueAs(EmployeeId other) {
		return other != null && this.id.equals(other.id);
	}

	@Override
	public String toString() {
		return id;
	}
}
