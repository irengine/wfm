package com.kwchina.wfm.domain.model.employee;

import com.kwchina.wfm.domain.common.ValueObject;

public enum JobStatus implements ValueObject<JobStatus> {
	HIRED,
	FIRED,
	LEFT,
	RETIED,
	UNKNOWN;

	@Override
	public boolean sameValueAs(JobStatus other) {
		return this.equals(other);
	}

}
