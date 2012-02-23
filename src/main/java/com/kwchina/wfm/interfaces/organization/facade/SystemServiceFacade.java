package com.kwchina.wfm.interfaces.organization.facade;

import java.util.List;
import java.util.Map;

import com.kwchina.wfm.interfaces.organization.dto.AttendanceTypePropertyDTO;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;

public interface SystemServiceFacade {

	void saveHoliday(SaveHolidayCommand command);
	Map<String, String> getHolidays(int year);
	
	void saveAttendanceTypeProperty(SaveAttendanceTypePropertyCommand command);
	List<AttendanceTypePropertyDTO> getAttendanceTypeProperties();
}
