package com.kwchina.wfm.domain.model.employee;

import java.util.Date;

import com.kwchina.wfm.domain.model.organization.Unit;

public class Job {

	private Unit unit;
//	private JobTitle title;
//	private List<JobPosition> positions;
	private JobStatus status;
	private Date effectDate;
	
	public Job() {
		
	}
	
	public Job(Unit unit, /*JobTitle title, List<JobPosition> positions, */JobStatus status, Date effectDate) {
		this.unit = unit;
//		this.title = title;
//		this.positions = positions;
		this.status = status;
		this.effectDate = effectDate;
	}
	
	/**
	 * @return the unit
	 */
	public Unit getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
//	/**
//	 * @return the title
//	 */
//	public JobTitle getTitle() {
//		return title;
//	}
//	/**
//	 * @param title the title to set
//	 */
//	public void setTitle(JobTitle title) {
//		this.title = title;
//	}
//	/**
//	 * @return the positionss
//	 */
//	public List<JobPosition> getPositions() {
//		return positions;
//	}
//	/**
//	 * @param positionss the positionss to set
//	 */
//	public void setPositions(List<JobPosition> positionss) {
//		this.positions = positionss;
//	}
	/**
	 * @return the status
	 */
	public JobStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(JobStatus status) {
		this.status = status;
	}
	/**
	 * @return the effectDate
	 */
	public Date getEffectDate() {
		return effectDate;
	}
	/**
	 * @param effectDate the effectDate to set
	 */
	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
}
