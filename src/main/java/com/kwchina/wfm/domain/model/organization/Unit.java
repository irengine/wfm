package com.kwchina.wfm.domain.model.organization;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kwchina.wfm.domain.model.shift.ShiftType;

@Entity
@Table(name="T_UNITS")
@NamedQueries({
	@NamedQuery(name = "unit.findRoot", query = "SELECT u FROM Unit u WHERE u.parent = null"),
	@NamedQuery(name = "unit.findAll", query = "SELECT u FROM Unit u WHERE u.enable = true order by u.left"),
	@NamedQuery(name = "unit.findAllChildren", query = "SELECT u FROM Unit u WHERE u.enable = true and u.left > :parentLeft and u.left < :parentRight order by u.left"),
	@NamedQuery(name = "unit.findAllAncestor", query = "SELECT u FROM Unit u WHERE u.enable = true and u.left < :leafLeft and u.right > :leafRight order by u.left")
})
public class Unit implements com.kwchina.wfm.domain.common.Entity<Unit>, PreferenceGetter {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@OneToMany(targetEntity=Unit.class, cascade=CascadeType.ALL, mappedBy="parent")
	private Collection<Unit> children = new LinkedHashSet<Unit>();
	
	@ManyToOne
	@JoinColumn(name="parentId")
	private Unit parent;

	@Column(name="leftId", nullable=false)
	private Long left = 0L;
	
	@Column(name="rightId", nullable=false)
	private Long right = 0L;
	
	@Column(nullable=false)
	private String name;
	
	@Column(unique=true, nullable=false)
	private String uriName;
	
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "units",
            targetEntity = User.class
        )
	private Collection<User> users = new LinkedHashSet<User>();;
	
	@ManyToOne
	@JoinColumn(name="shiftTypeId")
	private ShiftType shiftType;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="T_UNIT_PREFERENCES", joinColumns=@JoinColumn(name="unitId"))
	@AttributeOverrides({
		@AttributeOverride(name="key", column=@Column(name="xkey", nullable=false)),
		@AttributeOverride(name="value", column=@Column(name="xvalue")),
		@AttributeOverride(name="scope", column=@Column(name="xscope", nullable= false))
	})
    private Set<Preference> preferences = new LinkedHashSet<Preference>();
	
	@Column(nullable=false)
	private boolean enable;
	
	public Unit() {
		this.enable = true;
	}
	
	public Unit(String name) {
		this.name = name;
		this.uriName = name;
		this.enable = true;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Collection<Unit> getChildren() {
		return children;
	}

	protected void setChildren(Collection<Unit> children) {
		this.children = children;
	}

	@JsonIgnore
	public Unit getParent() {
		return parent;
	}

	public void setParent(Unit parent) {
		this.parent = parent;
	}

	public Long getLeft() {
		return left;
	}

	public void setLeft(Long left) {
		this.left = left;
	}

	public Long getRight() {
		return right;
	}

	public void setRight(Long right) {
		this.right = right;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		
		if (null == parent)
			setUriName(name);
		else
			setUriName(parent.getUriName() + "-" + name);
	}

	public String getUriName() {
		return uriName;
	}

	private void setUriName(String uriName) {
		this.uriName = uriName;
		
		for (Unit u : this.children) {
			u.setUriName(uriName + "-" + u.getName());
		}
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	public ShiftType getShiftType() {
		if (null == shiftType)
		{
			Unit parent = (Unit)getParent();
			if (null == parent)
				return null;
			else
				return parent.getShiftType();
		}
		return shiftType;
	}

	public void setShiftType(ShiftType shiftType) {
		this.shiftType = shiftType;
	}

	public Set<Preference> getPreferences() {
		return preferences;
	}

	public void setPreferences(Set<Preference> preferences) {
		this.preferences = preferences;
		
		for (Unit u : this.children) {
			Set<Preference> ups = u.getPreferences();
			for (Preference p : this.preferences) {
				Preference up = u.getPreference(p.getKey());
				if (null == up) {
					ups.add(new Preference(p.getKey(), p.getValue(), "I"));
				} else if (up.getScope().equals("I")) {
					ups.remove(up);
					ups.add(new Preference(p.getKey(), p.getValue(), "I"));
				}
			}
			u.setPreferences(ups);
		}
	}
	
	public Preference getPreference(String key) {
		for (Preference p : preferences) {
			if (p.getKey().equals(key)) {
				return p;
			}
		}
		
		return null;
//		Unit parent = (Unit)getParent();
//		if (null == parent)
//			return null;
//		else
//			return parent.getPreference(key);
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	/*
	 * add child
	 */
	public void addChild(Unit unit)
	{
		unit.setUriName(this.getUriName() + "-" + unit.getName());
		this.children.add(unit);
		unit.setParent(this);
	}
	
	/*
	 * remove child
	 */
	public void removeChild(Unit unit)
	{
		this.children.remove(unit);
		unit.setParent(null);
	}

	@Override
	public boolean sameIdentityAs(final Unit other) {
		return other != null && uriName.equals(other.uriName);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final Unit other = (Unit) object;
		return sameIdentityAs(other);
	}

	@Override
	public int hashCode() {
		return uriName.hashCode();
	}
}
