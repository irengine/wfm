package com.kwchina.wfm.interfaces.organization.facade;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.interfaces.organization.dto.UnitDTO;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUnitCommand;

@Component
public class UnitServiceFacadeImpl implements UnitServiceFacade {

	@Autowired
	UnitRepository unitRepository;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void loadSampleData() {
		Unit root = unitRepository.getRoot("S");
		
		Unit l1 = new Unit("L1");
		unitRepository.addChild(root, l1);
		
		Unit l11 = new Unit("L11");
		unitRepository.addChild(l1, l11);

		Unit l12 = new Unit("L12");
		unitRepository.addChild(l1, l12);

		Unit l2 = new Unit("L2");
		unitRepository.addChild(root, l2);
		
		Unit l21 = new Unit("L21");
		unitRepository.addChild(l2, l21);

		Unit l22 = new Unit("L22");
		unitRepository.addChild(l2, l22);

		Unit l23 = new Unit("L23");
		unitRepository.addChild(l2, l23);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public String getUnitsWithJson() {
		Unit root = unitRepository.findRoot();
		
		UnitDTO uo = new UnitDTO(root);
		List<UnitDTO> uos = new ArrayList<UnitDTO>();
		uos.add(uo); 
		
		StringWriter sw = new StringWriter();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(sw, uos);
		}
		catch(Exception e) {
			
		}
		
		return sw.toString();
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUnit(SaveUnitCommand command) {
		if (null == command.getId() || command.getId().equals(0))
		{
			if (null == command.getParentUnitId() || command.getParentUnitId().equals(0)) {
				unitRepository.getRoot(command.getName());
			}
			else {
				Unit parentUnit = unitRepository.findById(command.getParentUnitId());
				unitRepository.addChild(parentUnit, new Unit(command.getName()));
			}
		}
		else
		{
			Unit unit = unitRepository.findById(command.getId());
			unit.setName(command.getName());
			unitRepository.save(unit);
		}

	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Unit findById(Long id) {
		return unitRepository.findById(id);
	}
	
}
