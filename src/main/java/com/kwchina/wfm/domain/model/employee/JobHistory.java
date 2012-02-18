package com.kwchina.wfm.domain.model.employee;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

public class JobHistory {

	private List<JobChangeEvent> jobChangeEvents;

	public static JobHistory EMPTY = new JobHistory(Collections.<JobChangeEvent>emptyList());
	
	public JobHistory() {
		
	}
	
	public JobHistory(List<JobChangeEvent> jobChangeEvents) {
		Validate.notNull(jobChangeEvents);
		
		this.setJobChangeEvents(jobChangeEvents);
	}

	public List<JobChangeEvent> getJobChangeEvents() {
		return jobChangeEvents;
	}

	public void setJobChangeEvents(List<JobChangeEvent> jobChangeEvents) {
		this.jobChangeEvents = jobChangeEvents;
	}
}
