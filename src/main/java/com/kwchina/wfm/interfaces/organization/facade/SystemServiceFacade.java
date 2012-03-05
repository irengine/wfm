package com.kwchina.wfm.interfaces.organization.facade;

import java.util.Map;

import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveShiftTypeCommand;

public interface SystemServiceFacade {

	void saveHoliday(SaveHolidayCommand command);
	Map<String, String> getHolidays(int year);
	
	void saveAttendanceTypeProperty(SaveAttendanceTypePropertyCommand command);
	String queryAttendanceTypePropertiesWithJson();

	void saveEmployeeProperty(SaveEmployeePropertyCommand command);
	String queryEmployeePropertiesWithJson();

	void saveAttendanceType(SaveAttendanceTypeCommand command);
	String queryAttendanceTypesWithJson(QueryCommand command);
	AttendanceType findAttendanceTypeById(Long id);
	
	void saveShiftType(SaveShiftTypeCommand command);
	String queryShiftTypesWithJson(QueryCommand command);
	ShiftType findShiftTypeById(Long id);
}
