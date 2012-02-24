package com.kwchina.wfm.domain.model.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

import com.kwchina.wfm.domain.common.ValueObject;

@Entity
@Table(name="T_SYSTEM_PREFERENCES")
@NamedQueries({
	@NamedQuery(name = "systemPreference.findByScope", query = "SELECT sp FROM SystemPreference sp WHERE sp.scope = :scope order by sp.key"),
	@NamedQuery(name = "systemPreference.findByScopeAndKey", query = "SELECT sp FROM SystemPreference sp WHERE sp.scope = :scope AND sp.key = :key"),
	@NamedQuery(name = "systemPreference.findByScopeAndKeyOrValue", query = "SELECT sp FROM SystemPreference sp WHERE sp.scope = :scope AND (sp.key = :key OR sp.value = :key)")
})
public class SystemPreference implements com.kwchina.wfm.domain.common.Entity<SystemPreference> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="xkey", nullable=false)
	private String key;
	
	@Column
	private String type;
	
	@Column(name="xvalue")
	private String value;
	
	@Column(nullable=false)
	private ScopeType scope;
	
	public enum ScopeType implements ValueObject<ScopeType> {
		HOLIDAY,
		DAYCHANGED,
		ATTENDANCETYPE;

		@Override
		public boolean sameValueAs(ScopeType other) {
			return other != null && this.equals(other);
		}
	}
	
	public SystemPreference() {
		
	}

	public SystemPreference(ScopeType scope, String key, String type, String value) {
		Validate.notNull(scope);
		Validate.notNull(key);
		
		this.scope = scope;
		this.key = key;
		this.type = type;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ScopeType getScope() {
		return scope;
	}

	public void setScope(ScopeType scope) {
		this.scope = scope;
	}
	
	
	@Override
	public boolean sameIdentityAs(SystemPreference other) {
		return other != null && id.equals(other.id);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final SystemPreference other = (SystemPreference) object;
		return sameIdentityAs(other);
	}

	/**
	 * @return Hash code of tracking id.
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
