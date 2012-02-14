package com.kwchina.wfm.domain.model.organization;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name="T_EMPLOYEES")
public class Employee implements com.kwchina.wfm.domain.common.Entity<Employee> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private String code;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date birthday;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date beginDateOfWork;

	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date beginDateOfJob;

	@ManyToOne
	@JoinColumn(name="unitId")
	private Unit unit;
	
	@Column(nullable=false)
	private boolean enable;
	
	public Employee() {
		this.enable = true;
	}
	
	public Employee(String code, String name, Date birthday, Date beginDateOfWork, Date beginDateOfJob) {
		this.code = code;
		this.name = name;
		this.birthday = birthday;
		this.beginDateOfWork = beginDateOfWork;
		this.beginDateOfJob = beginDateOfJob;
		this.enable = true;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getBeginDateOfWork() {
		return beginDateOfWork;
	}

	public void setBeginDateOfWork(Date beginDateOfWork) {
		this.beginDateOfWork = beginDateOfWork;
	}

	public Date getBeginDateOfJob() {
		return beginDateOfJob;
	}

	public void setBeginDateOfJob(Date beginDateOfJob) {
		this.beginDateOfJob = beginDateOfJob;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	@Override
	public boolean sameIdentityAs(Employee other) {
		return other != null && code.equals(other.code);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final Employee other = (Employee) object;
		return sameIdentityAs(other);
	}

	/**
	 * @return Hash code of tracking id.
	 */
	@Override
	public int hashCode() {
		return code.hashCode();
	}
}
