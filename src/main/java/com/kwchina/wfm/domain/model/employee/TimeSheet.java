package com.kwchina.wfm.domain.model.employee;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.kwchina.wfm.domain.common.ValueObject;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.shift.AttendanceType;

@Entity
@Table(name="T_TIMESHEET")
public class TimeSheet implements com.kwchina.wfm.domain.common.Entity<TimeSheet> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="unitId")
	private Unit unit;
	
	@ManyToOne
	@JoinColumn(name="employeeId")
	private Employee employee;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date date;
	
	@Column
	private int beginTime;
	
	@Column
	private int endTime;
	
	@ManyToOne
	@JoinColumn(name="attendanceTypeId")
	private AttendanceType attendanceType;
	
	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private ActionType actionType;
	
	@Column(nullable=false)
	private boolean enable;
	
	@ManyToOne
	@JoinColumn(name="referTo")
	private TimeSheet referTo;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso=ISO.DATE_TIME)
	private Date createdAt;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso=ISO.DATE_TIME)
	private Date updatedAt;
	
	public TimeSheet() {
		this.enable = true;
	}
	
	public TimeSheet(Unit unit, Employee employee, Date date, int beginTime, int endTime, AttendanceType attendanceType, ActionType actionType) {
		this.unit = unit;
		this.employee = employee;
		this.date = date;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.attendanceType = attendanceType;
		this.actionType = actionType;
		this.enable = true;
	}
	
	public enum ActionType implements ValueObject<ActionType> {
		MONTH_PLAN,
		MONTH_PLAN_ADJUST,
		DAY_PLAN_ADJUST,
		ACTUAL;

		@Override
		public boolean sameValueAs(ActionType other) {
			return other != null && this.equals(other);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public AttendanceType getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(AttendanceType attendanceType) {
		this.attendanceType = attendanceType;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public TimeSheet getReferTo() {
		return referTo;
	}

	public void setReferTo(TimeSheet referTo) {
		this.referTo = referTo;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@PreUpdate
	@PrePersist
	public void updateTimeStamps() {
	    this.updatedAt = new Date();
	    if (this.createdAt == null) {
	      this.createdAt = this.updatedAt;
	    }
	}

	
	@Override
	public boolean sameIdentityAs(TimeSheet other) {
		return other != null && id.equals(other.id);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final TimeSheet other = (TimeSheet) object;
		return sameIdentityAs(other);
	}

	/**
	 * @return Hash code of tracking id.
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
