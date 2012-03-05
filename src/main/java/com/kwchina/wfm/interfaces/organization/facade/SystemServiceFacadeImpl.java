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
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
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
			Date v = calendar.getTime();
			if (hs.isSatisfiedBy(v))
				days.put(DateHelper.getString(v), "Holiday");
			else if (ws.isSatisfiedBy(v)) 
				days.put(DateHelper.getString(v), "Weekend");
			
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
			String[] names = StringUtils.split(command.getIds(), ActionCommand.ID_SEPARATOR);
			for(String name : names)
				systemPreferenceRepository.removeProperty(SystemPreference.ScopeType.ATTENDANCETYPE, name);
		}
	}

	private List<AttendanceTypePropertyDTO> getAttendanceTypeProperties() {
		
		List<AttendanceTypePropertyDTO> properties = new ArrayList<AttendanceTypePropertyDTO>();
		
		List<SystemPreference> preferences = systemPreferenceRepository.getProperties(SystemPreference.ScopeType.ATTENDANCETYPE);
		for (SystemPreference p : preferences) {
			properties.add(new AttendanceTypePropertyDTO(p.getKey(), p.getType(), p.getValue()));
		}
		
		return properties;
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryAttendanceTypePropertiesWithJson() {

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
			String[] names = StringUtils.split(command.getIds(), ActionCommand.ID_SEPARATOR);
			for(String name : names)
				systemPreferenceRepository.removeProperty(SystemPreference.ScopeType.EMPLOYEE, name);
		}
	}

	private List<EmployeePropertyDTO> getEmployeeProperties() {
		
		List<EmployeePropertyDTO> properties = new ArrayList<EmployeePropertyDTO>();
		
		List<SystemPreference> preferences = systemPreferenceRepository.getProperties(SystemPreference.ScopeType.EMPLOYEE);
		for (SystemPreference p : preferences) {
			properties.add(new EmployeePropertyDTO(p.getKey(), p.getType(), p.getValue()));
		}
		
		return properties;
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeePropertiesWithJson() {

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
			String[] ids = StringUtils.split(command.getIds(), ActionCommand.ID_SEPARATOR);
			for (String id : ids) {
				AttendanceType attendanceType = attendanceTypeRepository.findById(Long.parseLong(id));
				// TODO: logical delete
				attendanceTypeRepository.remove(attendanceType);
			}
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryAttendanceTypesWithJson(QueryCommand command) {
		List<String> conditions = new ArrayList<String>();
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", command.getSidx(), command.getSord());
		
		if (Boolean.parseBoolean(command.getSearch())) {
			whereClause = QueryHelper.getWhereClause(command.getFilters(), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = attendanceTypeRepository.getRowsCount(whereClause, true).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, command.getRows());
		pageHelper.setCurrentPage(command.getPage());
		
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
		if (command.getCommandType().equals(ActionCommand.ADD)) {
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
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			String[] ids = StringUtils.split(command.getIds(), ActionCommand.ID_SEPARATOR);
			for (String id : ids) {
				ShiftType shiftType = shiftTypeRepository.findById(Long.parseLong(id));
				// TODO: logical delete
				shiftTypeRepository.remove(shiftType);
			}
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryShiftTypesWithJson(QueryCommand command) {
		List<String> conditions = new ArrayList<String>();
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", command.getSidx(), command.getSord());
		
		if (Boolean.parseBoolean(command.getSearch())) {
			whereClause = QueryHelper.getWhereClause(command.getFilters(), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = shiftTypeRepository.getRowsCount(whereClause, true).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, command.getRows());
		pageHelper.setCurrentPage(command.getPage());
		
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
