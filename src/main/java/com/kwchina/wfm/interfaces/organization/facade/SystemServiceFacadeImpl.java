package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.shift.HolidaySpecification;
import com.kwchina.wfm.domain.model.shift.SystemPreference;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceFactory;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;
import com.kwchina.wfm.domain.model.shift.WeekendSpecification;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.organization.dto.AttendanceTypePropertyDTO;
import com.kwchina.wfm.interfaces.organization.web.command.ActionCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;

@Component
public class SystemServiceFacadeImpl implements SystemServiceFacade {
	
	@Autowired
	private SystemPreferenceRepository systemPreferenceRepository;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveHoliday(SaveHolidayCommand command) {

		if (command.getCommandType().equals(ActionCommand.ADD)) {
			if (StringUtils.isEmpty(command.getHoliday())) {
				systemPreferenceRepository.addDaysChanged(command.getDayChangedBefore(), command.getDayChangedAfter());
			}
			else {
				systemPreferenceRepository.addHoliday(command.getHoliday());
			}
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			if (StringUtils.isEmpty(command.getHoliday())) {
				systemPreferenceRepository.removeDaysChanged(command.getDayChangedBefore());
			}
			else {
				systemPreferenceRepository.removeHoliday(command.getHoliday());
			}
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public Map<String, String> getHolidays(int year) {
		
		String weekends = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getWeekends();
		List<String> holidays = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getHolidays();
		Map<String, String> daysChanged = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getDaysChanged();
		
		Map<String, String> days = new HashMap<String, String>();
		
		HolidaySpecification hs = new HolidaySpecification(holidays);
		WeekendSpecification ws = new WeekendSpecification(weekends, daysChanged);
		
		String s = String.format("%d-01-01", year);
		Date d = DateHelper.getDate(s);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);

		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		
		for(int i = 0; i< lastDay; i++) {
			Date x = calendar.getTime();
			if (hs.isSatisfiedBy(x))
				days.put(DateHelper.getString(x), "Holiday");
			else if (ws.isSatisfiedBy(x)) 
				days.put(DateHelper.getString(x), "Weekend");
			
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return days;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveAttendanceTypeProperty(SaveAttendanceTypePropertyCommand command) {
		
		if (command.getCommandType().equals(ActionCommand.ADD)) {
			systemPreferenceRepository.addAttendanceTypeProperty(command.getName(), command.getType(), command.getDescription());
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			systemPreferenceRepository.removeAttendanceTypeProperty(command.getName());
		}
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<AttendanceTypePropertyDTO> getAttendanceTypeProperties() {
		
		List<AttendanceTypePropertyDTO> properties = new ArrayList<AttendanceTypePropertyDTO>();
		
		List<SystemPreference> preferences = systemPreferenceRepository.getAttendanceTypeProperties();
		for (SystemPreference p : preferences) {
			properties.add(new AttendanceTypePropertyDTO(p.getKey(), p.getType(), p.getValue()));
		}
		
		return properties;
	}

}
