package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;

public interface SystemServiceFacade {

	String saveHoliday(SaveHolidayCommand command);
}
