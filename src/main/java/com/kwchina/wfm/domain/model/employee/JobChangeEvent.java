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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.kwchina.wfm.domain.common.DomainEvent;
import com.kwchina.wfm.domain.common.ValueObject;
import com.kwchina.wfm.domain.model.organization.Unit;

@Entity
@Table(name="T_EMPLOYEE_JOBCHANGEEVENTS")
@NamedQueries({
	@NamedQuery(name = "jobChangeEvent.findLast", query = "SELECT je FROM JobChangeEvent je WHERE je.employee = :employee AND je.endDate is null")
})
public class JobChangeEvent implements DomainEvent<JobChangeEvent> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@ManyToOne
	@JoinColumn(name="employeeId")
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name="unitId")
	private Unit unit;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date beginDate;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date endDate;
	
	public enum Type implements ValueObject<Type> {
		HIRE,
		FIRE,
		LEAVE,
		RETIE,
		TRANSFER,
		UNKNOWN;

		@Override
		public boolean sameValueAs(Type other) {
			return other != null && this.equals(other);
		}
		
	}
	
	public JobChangeEvent() {
		
	}
	
	public JobChangeEvent(Type type, Unit unit, Employee employee, Date beginDate, Date endDate) {
		this.type = type;
		this.unit = unit;
		this.employee = employee;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
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
	public boolean sameEventAs(JobChangeEvent other) {
		// TODO Auto-generated method stub
		return false;
	}
}
