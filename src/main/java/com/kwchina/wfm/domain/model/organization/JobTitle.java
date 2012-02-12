package com.kwchina.wfm.domain.model.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_JOBTITLES")
public class JobTitle implements com.kwchina.wfm.domain.common.Entity<JobTitle> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private String code;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private Long displayIndex = 0L;
	
	@Column
	private String displayName;

	public JobTitle() {

	}
	
	public JobTitle(String code, String name, Long displayIndex, String displayName, String strategyClassName) {
		this.code = code;
		this.name = name;
		this.displayIndex = displayIndex;
		this.displayName = displayName;
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

	public Long getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(Long displayIndex) {
		this.displayIndex = displayIndex;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public boolean sameIdentityAs(JobTitle other) {
		return other != null && code.equals(other.code);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final JobTitle other = (JobTitle) object;
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
