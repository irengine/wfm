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
		copy(root, units);
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
	
	public void removeChild(UnitDTO unitDTO)
	{
		this.children.remove(unitDTO);
	}
	
	public void copy(Unit unit)
	{
		setId(unit.getId().toString());
		setData(unit.getName());
		for(Unit child : unit.getChildren()) {
			UnitDTO unitDTO = new UnitDTO((Unit)child);
			addChild(unitDTO);
		}
	}

	public void copy(Unit unit, Collection<Unit> units)
	{
		setId(unit.getId().toString());
		setData(unit.getName());
		for(Unit child : unit.getChildren()) {
			if (isInclude(child, units)) {
			UnitDTO unitDTO = new UnitDTO((Unit)child);
			addChild(unitDTO);
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

