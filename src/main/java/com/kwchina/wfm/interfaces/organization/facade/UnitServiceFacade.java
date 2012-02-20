package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUnitCommand;


public interface UnitServiceFacade {

	void loadSampleData();
	String getUnitsWithJson();
	
	void saveUnit(SaveUnitCommand command);
	Unit findById(Long id);
}
