package com.kwchina.wfm.domain.model.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_SHIFTTYPES")
public class ShiftType implements com.kwchina.wfm.domain.common.Entity<ShiftType> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String name;
	
	@Column(nullable=false)
	private int displayIndex = 0;
	
	@Column
	private String displayName;
	
	@Column(nullable=false)
	private String strategyClassName;
	
	@Column(nullable=false)
	private String strategyClassParameters;
	
	public ShiftType() {

	}
	
	public ShiftType(String name, int displayIndex, String displayName, String strategyClassName, String strategyClassParameters) {
		this.name = name;
		this.displayIndex = displayIndex;
		this.displayName = displayName;
		this.strategyClassName = strategyClassName;
		this.strategyClassParameters = strategyClassParameters;
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

	public int getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(int displayIndex) {
		this.displayIndex = displayIndex;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStrategyClassName() {
		return strategyClassName;
	}

	public void setStrategyClassName(String strategyClassName) {
		this.strategyClassName = strategyClassName;
	}

	public String getStrategyClassParameters() {
		return strategyClassParameters;
	}

	public void setStrategyClassParameters(String strategyClassParameters) {
		this.strategyClassParameters = strategyClassParameters;
	}
	
	@Override
	public boolean sameIdentityAs(ShiftType other) {
		return other != null && this.name.equals(other.name);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final ShiftType other = (ShiftType) object;
		return sameIdentityAs(other);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
