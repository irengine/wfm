package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.organization.Unit;


public interface UnitServiceFacade {

	void loadSampleData();
	String getUnitsWithJson();
	String queryUnitsWithJson();
	
	Unit findById(Long id);
}
