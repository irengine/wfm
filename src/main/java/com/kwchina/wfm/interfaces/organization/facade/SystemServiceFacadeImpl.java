package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;

public class SystemServiceFacadeImpl implements SystemServiceFacade {

	private static List<String> holidays = new ArrayList<String>();
	private static Map<String, String> daysChanged = new HashMap<String, String>();
	
	@Override
	public String saveHoliday(SaveHolidayCommand command) {

		if (command.getType().equals("Add")) {
			if (StringUtils.isEmpty(command.getHoliday())) {
				holidays.add(command.getHoliday());
			}
			else {
				daysChanged.put(command.getDayChangedBefore(), command.getDayChangedAfter());
				daysChanged.put(command.getDayChangedAfter(), command.getDayChangedBefore());
			}
		}
		else if (command.getType().equals("Delete")) {
			if (StringUtils.isEmpty(command.getHoliday())) {
				holidays.remove(command.getHoliday());
			}
			else {
				daysChanged.remove(command.getDayChangedBefore());
				daysChanged.remove(command.getDayChangedAfter());
			}
		}
		
		List<Object> vs = new ArrayList<Object>();
		vs.add(holidays);
		vs.add(daysChanged);
		
		return JacksonHelper.getJson(vs);
	}

}
