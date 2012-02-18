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

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
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

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean sameEventAs(JobChangeEvent other) {
		// TODO Auto-generated method stub
		return false;
	}
}
