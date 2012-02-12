package com.kwchina.wfm.interfaces.organization.dto;

import java.util.ArrayList;
import java.util.List;

import com.kwchina.wfm.domain.model.organization.Unit;

public class UnitDTOs {

	private List<UnitDTO> data = new ArrayList<UnitDTO>();
	
	public List<UnitDTO> getData() {
		return data;
	}
	
	public void add(Unit unit) {
		getData().add(new UnitDTO(unit));
	}
	
}
