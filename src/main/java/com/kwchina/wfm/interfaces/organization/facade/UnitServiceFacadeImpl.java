package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.infrastructure.common.SecurityHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.organization.dto.UnitDTO;
import com.kwchina.wfm.interfaces.organization.web.command.ActionCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUnitCommand;

@Component
public class UnitServiceFacadeImpl implements UnitServiceFacade {

	@Autowired
	UnitRepository unitRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ShiftTypeRepository shiftTypeRepository;
	
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
		String name = SecurityHelper.getCurrentUserName();
		User user = userRepository.findByName(name);
		
		Unit root = unitRepository.findRoot();
		
		UnitDTO uo = null;
		if (name.equals("sysAdmin"))
			uo = new UnitDTO(root);
		else
			uo = new UnitDTO(root, user.getUnits());
		List<UnitDTO> uos = new ArrayList<UnitDTO>();
		uos.add(uo); 
		
		return JacksonHelper.getJson(uos);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUnit(SaveUnitCommand command) {
		if (command.getCommandType().equals(ActionCommand.ADD)) {
			if (null == command.getId() || command.getId().equals(0))
			{
				if (null == command.getParentUnitId() || command.getParentUnitId().equals(0)) {
					unitRepository.getRoot(command.getName());
				}
				else {
					Unit parentUnit = unitRepository.findById(command.getParentUnitId());
					Unit unit = new Unit(command.getName());
					unitRepository.addChild(parentUnit, unit);
				}
			}
			else
			{
				Unit unit = unitRepository.findById(command.getId());
				unit.setName(command.getName());
				if (null == command.getShiftTypeId() || command.getShiftTypeId().equals(0))
					unit.setShiftType(null);
				else {
					ShiftType shiftType = shiftTypeRepository.findById(command.getShiftTypeId());
					if (null != shiftType)
						unit.setShiftType(shiftType);
				}
				if (null != command.getProperties()) {
					Set<Preference> preferences = new HashSet<Preference>();
					for(Map.Entry<String, String> property : command.getProperties().entrySet()) {
						preferences.add(new Preference(property.getKey(), property.getValue()));
					}
					unit.setPreferences(preferences);
				}	
				unitRepository.save(unit);
			}
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			if (null != command.getId() && !command.getId().equals(0)) {
				Unit unit = unitRepository.findById(command.getId());
				unitRepository.disable(unit);
			}
				
		}
	}

	@Transactional(propagation=Propagation.SUPPORTS)
	public Unit findById(Long id) {
		return unitRepository.findById(id);
	}
	
}
