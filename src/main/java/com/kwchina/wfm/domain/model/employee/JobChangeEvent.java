package com.kwchina.wfm.domain.model.employee;

import java.util.Date;

import com.kwchina.wfm.domain.common.DomainEvent;
import com.kwchina.wfm.domain.common.ValueObject;
import com.kwchina.wfm.domain.model.organization.Unit;

public class JobChangeEvent implements DomainEvent<JobChangeEvent> {
	
	private Long id;
	private Type type;
	private Unit unit;
	private Date beginDate;
	private Date endDate;
	
	public enum Type implements ValueObject<Type> {
		HIRE,
		FIRE,
		LEAVE,
		RETIE,
		UNKNOWN;

		@Override
		public boolean sameValueAs(Type other) {
			return other != null && this.equals(other);
		}
		
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
