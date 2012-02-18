package com.kwchina.wfm.domain.model.employee;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.Validate;

import com.kwchina.wfm.domain.model.organization.Unit;

@Embeddable
public class Job {

	@ManyToOne(optional=true)
	@JoinColumn(name="unitId")
	private Unit unit;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="jobTitleId")
	private JobTitle title;
	
	@ElementCollection
	@CollectionTable(name="T_EMPLOYEE_JOBPOSITIONS", joinColumns=@JoinColumn(name="employeeId"))
	private List<JobPosition> positions;
	
	private JobStatus status;
	
	private Date effectDate;
	
	public Job() {
		
	}
	
	public Job(Unit unit, JobTitle title, List<JobPosition> positions, JobStatus status, Date effectDate) {
		Validate.notNull(unit);
		Validate.notNull(status);
		Validate.notNull(effectDate);
		
		this.unit = unit;
		this.title = title;
		this.positions = positions;
		this.status = status;
		this.effectDate = effectDate;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public JobTitle getTitle() {
		return title;
	}

	public void setTitle(JobTitle title) {
		this.title = title;
	}

	public List<JobPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<JobPosition> positionss) {
		this.positions = positionss;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
}
