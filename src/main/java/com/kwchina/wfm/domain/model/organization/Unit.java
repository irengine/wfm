package com.kwchina.wfm.domain.model.organization;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
	@NamedQuery(name = "unit.findAllChildren", query = "SELECT u FROM Unit u WHERE u.enable = true and u.left > :parentLeft and u.left < :parentRight order by u.left"),
	@NamedQuery(name = "unit.findAllAncestor", query = "SELECT u FROM Unit u WHERE u.enalbe = true and u.left < :leafLeft and u.right > :leafRight order by u.left")
})
public class Unit implements com.kwchina.wfm.domain.common.Entity<Unit> {

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
	
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "units",
            targetEntity = User.class
        )
	private Collection<User> users = new LinkedHashSet<User>();;
	
	@ManyToOne
	@JoinColumn(name="shiftTypeId")
	private ShiftType shiftType;
	
	@Column(nullable=false)
	private boolean enable;
	
	public Unit() {
		this.enable = true;
	}
	
	public Unit(String name) {
		this.name = name;
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

	public void setChildren(Collection<Unit> children) {
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
	}

	/**
	 * @return the users
	 */
	public Collection<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
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
				return ((Unit)getParent()).getShiftType();
		}
		return shiftType;
	}

	public void setShiftType(ShiftType shiftType) {
		this.shiftType = shiftType;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public void addChild(Unit unit)
	{
		this.children.add(unit);
		unit.setParent(this);
	}
	
	public void removeChild(Unit unit)
	{
		this.children.remove(unit);
		unit.setParent(null);
	}

	@Override
	public boolean sameIdentityAs(final Unit other) {
		return other != null && id.equals(other.id);
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

	/**
	 * @return Hash code of tracking id.
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
