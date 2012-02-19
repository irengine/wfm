package com.kwchina.wfm.domain.model.employee;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.kwchina.wfm.domain.common.ValueObject;
import com.kwchina.wfm.domain.model.organization.Unit;

@Embeddable
public class Job implements ValueObject<Job> {

	private static final long serialVersionUID = -1727715418077422312L;

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
	
	public static Job UNKNOWN = new Job(null, null, Collections.<JobPosition>emptyList(), JobStatus.UNKNOWN, null);
	
	public Job() {
		
	}
	
	public Job(Unit unit, JobTitle title, List<JobPosition> positions, JobStatus status, Date effectDate) {
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
	
	@Override
	public boolean sameValueAs(final Job other) {
		return other != null
				&& new EqualsBuilder()
					.append(this.unit, other.unit)
					.append(this.title, other.title)
					.append(this.positions, other.positions)
					.append(this.status, other.status)
					.append(this.effectDate, other.effectDate)
					.isEquals();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final Job other = (Job) o;

		return sameValueAs(other);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.unit)
			.append(this.title)
			.append(this.positions)
			.append(this.status)
			.append(this.effectDate)
			.toHashCode();
	}
}
