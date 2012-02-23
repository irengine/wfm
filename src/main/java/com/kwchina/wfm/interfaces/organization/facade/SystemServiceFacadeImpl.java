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

import com.kwchina.wfm.domain.model.shift.HolidaySpecification;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;
import com.kwchina.wfm.domain.model.shift.WeekendSpecification;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;

@Component
public class SystemServiceFacadeImpl implements SystemServiceFacade {
	
	@Autowired
	private SystemPreferenceRepository systemPreferenceRepository;
	
	@Override
	public void saveHoliday(SaveHolidayCommand command) {

		if (command.getType().equals(SaveHolidayCommand.ADD)) {
			if (StringUtils.isEmpty(command.getHoliday())) {
				systemPreferenceRepository.addDaysChanged(command.getDayChangedBefore(), command.getDayChangedAfter());
			}
			else {
				systemPreferenceRepository.addHoliday(command.getHoliday());
			}
		}
		else if (command.getType().equals(SaveHolidayCommand.DELETE)) {
			if (StringUtils.isEmpty(command.getHoliday())) {
				systemPreferenceRepository.removeDaysChanged(command.getDayChangedBefore(), command.getDayChangedAfter());
			}
			else {
				systemPreferenceRepository.removeHoliday(command.getHoliday());
			}
		}
	}
	
	private String weekends = "1,7";
	private List<String> holidays = new ArrayList<String>();
	private Map<String, String> daysChanged = new HashMap<String, String>();
	
	@Override
	public Map<String, String> getHolidays(int year) {
		
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

}
