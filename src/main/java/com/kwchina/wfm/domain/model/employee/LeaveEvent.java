package com.kwchina.wfm.domain.model.employee;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.kwchina.wfm.domain.model.shift.AttendanceType;

@Entity
@Table(name="T_EMPLOYEE_LEAVEEVENTS")
public class LeaveEvent implements Serializable, com.kwchina.wfm.domain.common.DomainEvent<LeaveEvent> {

	private static final long serialVersionUID = -9207970537805968317L;

	@Id
	@AttributeOverrides({
		@AttributeOverride(name = "employee",	column = @Column(name="employeeId")),
		@AttributeOverride(name = "attendanceType",	column = @Column(name="attendanceTypeId")),
		@AttributeOverride(name = "beginDate",	column = @Column(name="beginDate")),
		@AttributeOverride(name = "endDate",	column = @Column(name="endDate"))
	})
	
	@ManyToOne
	@JoinColumn(name="employeeId")
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name="attendanceTypeId")
	private AttendanceType attendanceType;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date beginDate;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date endDate;
	
	public LeaveEvent() {
		
	}
	
	public LeaveEvent(Employee employee, AttendanceType attendanceType, Date beginDate, Date endDate) {
		this.employee = employee;
		this.attendanceType = attendanceType;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}
	
	public AttendanceType getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(AttendanceType attendanceType) {
		this.attendanceType = attendanceType;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final LeaveEvent event = (LeaveEvent) o;

		return sameEventAs(event);
	}

	@Override
	public boolean sameEventAs(final LeaveEvent other) {
		return other != null
				&& new EqualsBuilder().append(this.getEmployee(), other.getEmployee())
						.append(this.getAttendanceType(), other.getAttendanceType())
						.append(this.getBeginDate(), other.getBeginDate())
						.append(this.getEndDate(), other.getEndDate()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getEmployee()).append(getAttendanceType())
				.append(getBeginDate()).append(getEndDate()).toHashCode();
	}

}
