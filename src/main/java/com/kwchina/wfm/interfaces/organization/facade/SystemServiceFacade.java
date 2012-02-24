package com.kwchina.wfm.interfaces.organization.facade;

import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.interfaces.organization.dto.AttendanceTypePropertyDTO;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveShiftTypeCommand;

public interface SystemServiceFacade {

	void saveHoliday(SaveHolidayCommand command);
	Map<String, String> getHolidays(int year);
	
	void saveAttendanceTypeProperty(SaveAttendanceTypePropertyCommand command);
	List<AttendanceTypePropertyDTO> getAttendanceTypeProperties();
	
	void saveAttendanceType(SaveAttendanceTypeCommand command);
	List<AttendanceType> getAttendanceTypes();
	AttendanceType findAttendanceTypeById(Long id);
	
	void saveShiftType(SaveShiftTypeCommand command);
	List<ShiftType> getShiftTypes();
	ShiftType findShiftTypeById(Long id);
}
