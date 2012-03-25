package com.kwchina.wfm.domain.model.employee;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

import com.kwchina.wfm.domain.common.ValueObject;
import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.organization.PreferenceGetter;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.shift.ShiftType;

@Entity
@Table(name="T_EMPLOYEES")
@NamedQueries({
	@NamedQuery(name = "employee.findByCode", query = "SELECT e FROM Employee e WHERE e.employeeId.id = :code"),
	@NamedQuery(name = "employee.findByUnitId", query = "SELECT e FROM Employee e WHERE e.enable = true and e.job.unit.id = :unitId order by e.employeeId"),
	@NamedQuery(name = "employee.findAll", query = "SELECT e FROM Employee e WHERE e.enable = true order by e.employeeId"),
	@NamedQuery(name = "employee.findAllByUnitId", query = "SELECT e FROM Employee e, Unit u WHERE e.enable = true and u.id = :unitId AND u.left <= e.job.unit.left AND u.right >= e.job.unit.right order by e.employeeId")
})
public class Employee implements com.kwchina.wfm.domain.common.Entity<Employee>, PreferenceGetter {

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
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	public enum Gender implements ValueObject<Gender> {
		MALE,
		FEMALE;

		@Override
		public boolean sameValueAs(Gender other) {
			return other != null && this.equals(other);
		}
	}
	
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
	@JoinColumn(name="shiftTypeId")
	private ShiftType shiftType;
	
	@Embedded
	private Job job;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="T_EMPLOYEE_PREFERENCES", joinColumns=@JoinColumn(name="employeeId"))
	@AttributeOverrides({
		@AttributeOverride(name="key", column=@Column(name="xkey", nullable=false)),
		@AttributeOverride(name="value", column=@Column(name="xvalue"))
	})
    private Set<Preference> preferences;
	
	@ElementCollection
	@CollectionTable(name="T_EMPLOYEE_VACATIONS", joinColumns=@JoinColumn(name="employeeId"))
    private Set<Vacation> vacations;
	
	@Column(nullable=false)
	private boolean enable;
	
	public Employee() {
		this.enable = true;
	}
	
	public Employee(EmployeeId employeeId, String name, Gender gender, Date birthday, Date beginDateOfWork, Date beginDateOfJob) {
		this.employeeId = employeeId;
		this.name = name;
		this.gender = gender;
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
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
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
	
	public ShiftType getShiftType() {
		return shiftType;
	}

	public void setShiftType(ShiftType shiftType) {
		this.shiftType = shiftType;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Set<Preference> getPreferences() {
		return preferences;
	}

	public void setPreferences(Set<Preference> preferences) {
		this.preferences = preferences;
	}
	
	public String getPreference(String key) {
		for (Preference p : preferences) {
			if (p.getKey().equals(key)) {
				return p.getValue();
			}
		}
		
		return null;
	}

	public Set<Vacation> getVacations() {
		return vacations;
	}

	public void setVacations(Set<Vacation> vacations) {
		this.vacations = vacations;
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
	 * @return Hash code of employee id.
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
