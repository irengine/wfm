package com.kwchina.wfm.domain.model.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="T_UNITS")
@NamedQueries({
	@NamedQuery(name = "unit.findAll", query = "SELECT u FROM Unit u"),
	@NamedQuery(name = "unit.findByName", query = "SELECT u FROM Unit u WHERE u.name = :name"),
	@NamedQuery(name = "unit.findRoot", query = "SELECT u FROM Unit u WHERE u.parent = null"),
	@NamedQuery(name = "unit.findAllChildren", query = "SELECT u FROM Unit u WHERE u.left > :parentLeft and u.left < :parentRight order by u.left"),
	@NamedQuery(name = "unit.findAllAncestor", query = "SELECT u FROM Unit u WHERE u.left < :leafLeft and u.right > :leafRight order by u.left")
})
public class Unit extends Node  implements com.kwchina.wfm.domain.common.Entity<Unit> {

	@Column(unique = true)
	private String name;
	
	@ManyToOne
	private ShiftType shiftType;

	public Unit() {
		
	}
	
	public Unit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public boolean sameIdentityAs(final Unit other) {
		return other != null && name.equals(other.name);
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
		return name.hashCode();
	}

}
