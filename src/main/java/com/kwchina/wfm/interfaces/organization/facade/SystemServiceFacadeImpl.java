package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.domain.model.shift.HolidaySpecification;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.domain.model.shift.SystemPreference;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceFactory;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;
import com.kwchina.wfm.domain.model.shift.WeekendSpecification;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.Page;
import com.kwchina.wfm.interfaces.common.PageHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.dto.AttendanceTypePropertyDTO;
import com.kwchina.wfm.interfaces.organization.dto.EmployeePropertyDTO;
import com.kwchina.wfm.interfaces.organization.web.command.ActionCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveShiftTypeCommand;

@Component
public class SystemServiceFacadeImpl implements SystemServiceFacade {
	
	@Autowired
	private SystemPreferenceRepository systemPreferenceRepository;
	
	@Autowired
	private AttendanceTypeRepository attendanceTypeRepository;
	
	@Autowired
	private ShiftTypeRepository shiftTypeRepository;
	
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
			systemPreferenceRepository.addProperty(SystemPreference.ScopeType.ATTENDANCETYPE, command.getName(), command.getType(), command.getDescription());
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			systemPreferenceRepository.removeProperty(SystemPreference.ScopeType.ATTENDANCETYPE, command.getName());
		}
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<AttendanceTypePropertyDTO> getAttendanceTypeProperties() {
		
		List<AttendanceTypePropertyDTO> properties = new ArrayList<AttendanceTypePropertyDTO>();
		
		List<SystemPreference> preferences = systemPreferenceRepository.getProperties(SystemPreference.ScopeType.ATTENDANCETYPE);
		for (SystemPreference p : preferences) {
			properties.add(new AttendanceTypePropertyDTO(p.getKey(), p.getType(), p.getValue()));
		}
		
		return properties;
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryAttendanceTypePropertiesWithJson(Map<String, String> parameters,
			int currentPage, int pageSize, List<String> conditions) {

		List<AttendanceTypePropertyDTO> rows = getAttendanceTypeProperties();
		Page page = new Page(1, 1, rows.size(), rows);
		
		return JacksonHelper.getJson(page);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEmployeeProperty(SaveEmployeePropertyCommand command) {
		
		if (command.getCommandType().equals(ActionCommand.ADD)) {
			systemPreferenceRepository.addProperty(SystemPreference.ScopeType.EMPLOYEE, command.getName(), command.getType(), command.getDescription());
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			systemPreferenceRepository.removeProperty(SystemPreference.ScopeType.EMPLOYEE, command.getName());
		}
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<EmployeePropertyDTO> getEmployeeProperties() {
		
		List<EmployeePropertyDTO> properties = new ArrayList<EmployeePropertyDTO>();
		
		List<SystemPreference> preferences = systemPreferenceRepository.getProperties(SystemPreference.ScopeType.EMPLOYEE);
		for (SystemPreference p : preferences) {
			properties.add(new EmployeePropertyDTO(p.getKey(), p.getType(), p.getValue()));
		}
		
		return properties;
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeePropertiesWithJson(Map<String, String> parameters,
			int currentPage, int pageSize, List<String> conditions) {

		List<EmployeePropertyDTO> rows = getEmployeeProperties();
		Page page = new Page(1, 1, rows.size(), rows);
		
		return JacksonHelper.getJson(page);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveAttendanceType(SaveAttendanceTypeCommand command) {
		
		if (command.getCommandType().equals(ActionCommand.ADD)) {
			AttendanceType attendanceType;
			if (null == command.getId() || command.getId().equals(0))
				attendanceType = new AttendanceType();
			else
				attendanceType = attendanceTypeRepository.findById(command.getId());
			
			attendanceType.setName(command.getName());
			attendanceType.setBeginTime(command.getBeginTime());
			attendanceType.setEndTime(command.getEndTime());
			
			Set<Preference> preferences = new HashSet<Preference>();
			for(Map.Entry<String, String> property : command.getProperties().entrySet()) {
				preferences.add(new Preference(property.getKey(), property.getValue()));
			}
			attendanceType.setPreferences(preferences);
			attendanceTypeRepository.save(attendanceType);		
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			if (null != command.getId() && !command.getId().equals(0)) {
				AttendanceType attendanceType = attendanceTypeRepository.findById(command.getId());
				attendanceTypeRepository.remove(attendanceType);
			}
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryAttendanceTypesWithJson(Map<String, String> parameters,
			int currentPage, int pageSize, List<String> conditions) {
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", parameters.get(QueryHelper.SORT_FIELD), parameters.get(QueryHelper.SORT_DIRECTION));
		
		if (Boolean.parseBoolean(parameters.get(QueryHelper.IS_INCLUDE_CONDITION))) {
			whereClause = QueryHelper.getWhereClause(parameters.get(QueryHelper.FILTERS), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = attendanceTypeRepository.getRowsCount(whereClause, true).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, pageSize);
		pageHelper.setCurrentPage(currentPage);
		
		List<AttendanceType> rows = attendanceTypeRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize(), true);
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		
		return JacksonHelper.getJson(page);
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public AttendanceType findAttendanceTypeById(Long id) {
		return attendanceTypeRepository.findById(id);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveShiftType(SaveShiftTypeCommand command) {
		ShiftType shiftType;
		if (null == command.getId() || command.getId().equals(0))
			shiftType = new ShiftType();
		else
			shiftType = shiftTypeRepository.findById(command.getId());

		shiftType.setName(command.getName());
		shiftType.setDisplayIndex(command.getDisplayIndex());
		shiftType.setDisplayName(command.getDisplayName());
		shiftType.setStrategyClassName(command.getStrategyClassName());
		shiftType.setStrategyClassParameters(command.getStrategyClassParameters());
		
		shiftTypeRepository.save(shiftType);		
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryShiftTypesWithJson(Map<String, String> parameters,
			int currentPage, int pageSize, List<String> conditions) {
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", parameters.get(QueryHelper.SORT_FIELD), parameters.get(QueryHelper.SORT_DIRECTION));
		
		if (Boolean.parseBoolean(parameters.get(QueryHelper.IS_INCLUDE_CONDITION))) {
			whereClause = QueryHelper.getWhereClause(parameters.get(QueryHelper.FILTERS), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = shiftTypeRepository.getRowsCount(whereClause, true).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, pageSize);
		pageHelper.setCurrentPage(currentPage);
		
		List<ShiftType> rows = shiftTypeRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize(), true);
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		
		return JacksonHelper.getJson(page);
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public ShiftType findShiftTypeById(Long id) {
		return shiftTypeRepository.findById(id);
	}
}
