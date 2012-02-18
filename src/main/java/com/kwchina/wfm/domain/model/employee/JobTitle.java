package com.kwchina.wfm.domain.model.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

@Entity
@Table(name="T_JOBTITLES")
public class JobTitle implements com.kwchina.wfm.domain.common.Entity<JobTitle> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String code;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private Long level = 0L;
	
	@Column(nullable=false)
	private boolean enable;

	public JobTitle() {
		this.enable = true;
	}
	
	public JobTitle(String code, String name, Long level) {
		Validate.notNull(code);
		Validate.notNull(name);
		Validate.notNull(level);
		
		this.code = code;
		this.name = name;
		this.level = level;
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

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
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
