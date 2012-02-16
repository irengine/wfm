package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.organization.Unit;


public interface UnitServiceFacade {

	void loadSampleData();
	String getUnitsWithJson();
	
	void saveUnit(Unit unit, Long parentUnitId);
	Unit findById(Long id);
}
