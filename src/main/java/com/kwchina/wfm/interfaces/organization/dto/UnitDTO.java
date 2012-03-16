package com.kwchina.wfm.interfaces.organization.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

import com.kwchina.wfm.domain.model.organization.Unit;

public class UnitDTO {
	
	private HashMap<String, String> attr = new HashMap<String, String>();
	private String data;
	private Collection<UnitDTO> children = new LinkedHashSet<UnitDTO>();
	
	public UnitDTO() {
		
	}
	
	public UnitDTO(Unit root) {
		copy(root);
	}

	public UnitDTO(Unit root, Collection<Unit> units) {
		setId(root.getId().toString());
		setData(root.getName());
		
		copy(root, null, units);
	}

	public void setId(String id) {
		attr.put("id", id);
	}

	public HashMap<String, String> getAttr() {
		return attr;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Collection<UnitDTO> getChildren() {
		return children;
	}

	public void setChildren(Collection<UnitDTO> children) {
		this.children = children;
	}
	
	public void addChild(UnitDTO unitDTO)
	{
		this.children.add(unitDTO);
	}
	
	public void addChild(UnitDTO parentDTO, UnitDTO unitDTO)
	{
		parentDTO.children.add(unitDTO);
	}
	
	public void removeChild(UnitDTO unitDTO)
	{
		this.children.remove(unitDTO);
	}
	
	/*
	 * Copy all units into tree
	 */
	public void copy(Unit unit)
	{
		setId(unit.getId().toString());
		setData(unit.getName());
		for(Unit child : unit.getChildren()) {
			// TODO: using filter instead
			if (child.isEnable()) {
				UnitDTO unitDTO = new UnitDTO(child);
				addChild(unitDTO);
			}
		}
	}

	/*
	 * Only copy unit in mapping into tree
	 */
	public void copy(Unit unit, UnitDTO parentDTO, Collection<Unit> units)
	{
		if (null == parentDTO)
			parentDTO = this;
		
		for(Unit child : unit.getChildren()) {
			// TODO: using filter instead
			if (child.isEnable()) {
				if (isInclude(child, units)) {
					UnitDTO unitDTO = new UnitDTO(child, units);
					addChild(parentDTO, unitDTO);
				}
				else {
					copy(child, parentDTO, units);
				}
			}
		}
	}
	
	private boolean isInclude(Unit unit, Collection<Unit> units) {
		
		for (Unit u : units) {
			if (u.getId().equals(unit.getId()))
				return true;
		}
		
		return false;
	}

}

