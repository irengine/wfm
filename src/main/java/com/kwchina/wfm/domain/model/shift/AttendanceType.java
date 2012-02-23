package com.kwchina.wfm.domain.model.shift;

import java.util.Collections;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.kwchina.wfm.domain.model.organization.Preference;

@Entity
@Table(name="T_ATTENDANCE_TYPES")
public class AttendanceType implements com.kwchina.wfm.domain.common.Entity<AttendanceType> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String name;
	
	@Column
	private int beginHour;
	
	@Column
	private int endHour;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="T_ATTENDANCE_TYPE_PREFERENCES", joinColumns=@JoinColumn(name="attendanceTypeId"))
	@AttributeOverrides({
		@AttributeOverride(name="key", column=@Column(name="xkey", nullable=false)),
		@AttributeOverride(name="value", column=@Column(name="xvalue"))
	})
    private Set<Preference> preferences;
	
	public AttendanceType() {
		
	}
	
	public AttendanceType(String name, int beginHour, int endHour) {
		this.name = name;
		this.beginHour = beginHour;
		this.endHour = endHour;
		this.preferences = Collections.<Preference>emptySet();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBeginHour() {
		return beginHour;
	}

	public void setBeginHour(int beginHour) {
		this.beginHour = beginHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	
	public Set<Preference> getPreferences() {
		return preferences;
	}

	public void setPreferences(Set<Preference> preferences) {
		this.preferences = preferences;
	}

	@Override
	public boolean sameIdentityAs(final AttendanceType other) {
		return other != null && this.name.equals(other.name);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final AttendanceType other = (AttendanceType) object;
		return sameIdentityAs(other);
	}

	/**
	 * @return Hash code of tracking id.
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

}
