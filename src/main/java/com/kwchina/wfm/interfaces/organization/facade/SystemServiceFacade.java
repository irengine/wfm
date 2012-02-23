package com.kwchina.wfm.interfaces.organization.facade;

import java.util.Map;

import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;

public interface SystemServiceFacade {

	void saveHoliday(SaveHolidayCommand command);

	Map<String, String> getHolidays(int year);
}
