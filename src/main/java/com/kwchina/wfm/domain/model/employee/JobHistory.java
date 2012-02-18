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

	/**
	 * @return the jobChangeEvents
	 */
	public List<JobChangeEvent> getJobChangeEvents() {
		return jobChangeEvents;
	}

	/**
	 * @param jobChangeEvents the jobChangeEvents to set
	 */
	public void setJobChangeEvents(List<JobChangeEvent> jobChangeEvents) {
		this.jobChangeEvents = jobChangeEvents;
	}
}
