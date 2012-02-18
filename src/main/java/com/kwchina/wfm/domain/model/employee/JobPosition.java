package com.kwchina.wfm.domain.model.employee;

import javax.persistence.Embeddable;

import org.apache.commons.lang.Validate;

import com.kwchina.wfm.domain.common.ValueObject;

@Embeddable
public class JobPosition implements ValueObject<JobPosition> {
	
	private static final long serialVersionUID = -2085272637958850968L;

	private String code;
	
	public JobPosition() {
		
	}
	
	public JobPosition(String code) {
		Validate.notNull(code);
		
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		JobPosition other = (JobPosition) o;

		return sameValueAs(other);
	}

	@Override
	public int hashCode() {
		return code.hashCode();
	}

	@Override
	public boolean sameValueAs(JobPosition other) {
		return other != null && this.code.equals(other.code);
	}

	@Override
	public String toString() {
		return code;
	}

}
