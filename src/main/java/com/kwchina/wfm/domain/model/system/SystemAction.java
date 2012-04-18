package com.kwchina.wfm.domain.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.Validate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.kwchina.wfm.domain.common.ValueObject;

@Entity
@Table(name="T_SYSTEM_ACTIONS", uniqueConstraints= @UniqueConstraint(columnNames = {"xkey", "scope"}) )
@NamedQueries({
	@NamedQuery(name = "systemAction.findByScope", query = "SELECT sp FROM SystemAction sp WHERE sp.scope = :scope order by sp.key"),
	@NamedQuery(name = "systemAction.findByScopeAndKey", query = "SELECT sp FROM SystemAction sp WHERE sp.scope = :scope AND sp.key = :key"),
	@NamedQuery(name = "systemAction.findByScopeAndKeyOrValue", query = "SELECT sp FROM SystemAction sp WHERE sp.scope = :scope AND (sp.key = :key OR sp.value = :key)")
})

public class SystemAction implements com.kwchina.wfm.domain.common.Entity<SystemAction> {
	
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
	@Enumerated(EnumType.STRING)
	private ScopeType scope;
	
	public enum ScopeType implements ValueObject<ScopeType> {
		IMPORT,
		MONTH_PLAN,
		DAY_PLAN;
		
		@Override
		public boolean sameValueAs(ScopeType other) {
			return other != null && this.equals(other);
		}
	}
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso=ISO.DATE_TIME)
	private Date createdAt;
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso=ISO.DATE_TIME)
	private Date updatedAt;
	
	public SystemAction() {
		
	}

	public SystemAction(ScopeType scope, String key, String type, String value) {
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
	
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@PreUpdate
	@PrePersist
	public void updateTimeStamps() {
	    this.updatedAt = new Date();
	    if (this.createdAt == null) {
	      this.createdAt = this.updatedAt;
	    }
	}

	@Override
	public boolean sameIdentityAs(SystemAction other) {
		return other != null && id.equals(other.id);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final SystemAction other = (SystemAction) object;
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
