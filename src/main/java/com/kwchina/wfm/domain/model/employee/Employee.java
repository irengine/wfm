package com.kwchina.wfm.domain.model.employee;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.kwchina.wfm.domain.model.organization.Unit;

@Entity
@Table(name="T_EMPLOYEES")
public class Employee implements com.kwchina.wfm.domain.common.Entity<Employee> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name="id", column = @Column(name="employeeId", nullable=false, unique=true, updatable=false) )
		} )
	private EmployeeId employeeId;
	
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
	
	@Embedded
	private Job job;
	
	@Column(nullable=false)
	private boolean enable;
	
	public Employee() {
		this.enable = true;
	}
	
	public Employee(EmployeeId employeeId, String name, Date birthday, Date beginDateOfWork, Date beginDateOfJob) {
		this.employeeId = employeeId;
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

	public EmployeeId getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(EmployeeId employeeId) {
		this.employeeId = employeeId;
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
	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	@Override
	public boolean sameIdentityAs(Employee other) {
		return other != null && employeeId.equals(other.employeeId);
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
		return employeeId.hashCode();
	}
	
	public void hire(Unit unit, JobTitle title, List<JobPosition> positions, Date effectDate) {
		Job job = new Job(unit, title, positions, JobStatus.HIRED, effectDate);
		this.setJob(job);
	}
}
