package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
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

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeId;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.employee.Job;
import com.kwchina.wfm.domain.model.employee.JobStatus;
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.domain.model.employee.TimeSheetRepository;
import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.organization.PreferenceGetter;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceFactory;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.Page;
import com.kwchina.wfm.interfaces.common.PageHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.common.ReportHelper;
import com.kwchina.wfm.interfaces.organization.dto.TimeSheetDTO;
import com.kwchina.wfm.interfaces.organization.web.command.ActionCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveTimeSheetRecordCommand;

@Component
public class EmployeeServiceFacadeImpl implements EmployeeServiceFacade {

	@Autowired
	UnitRepository unitRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	ShiftTypeRepository shiftTypeRepository;
	
	@Autowired
	AttendanceTypeRepository attendanceTypeRepository;
	
	@Autowired
	TimeSheetRepository timeSheetRepository;
	
	@Autowired
	SystemPreferenceRepository systemPreferenceRepository;
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeesWithJson(QueryCommand command) {
		List<String> conditions = new ArrayList<String>();
		if (!StringUtils.isEmpty(command.getUnitId())) {
			Unit unit = unitRepository.findById(Long.parseLong(command.getUnitId()));
			String left = String.format("job.unit.left >= %d", unit.getLeft());
			conditions.add(left);
			String right = String.format("job.unit.right <= %d", unit.getRight());
			conditions.add(right);
		}
		
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", command.getSidx(), command.getSord());
		
		if (Boolean.parseBoolean(command.getSearch())) {
			whereClause = QueryHelper.getWhereClause(command.getFilters(), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = employeeRepository.getRowsCount(whereClause).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, command.getRows());
		pageHelper.setCurrentPage(command.getPage());
		
		List<Employee> rows = employeeRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize());
		
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		return JacksonHelper.getEmployeeJsonWithFilters(page);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEmployee(SaveEmployeeCommand command) {
		
		if (command.getCommandType().equals(ActionCommand.ADD)) {
			Employee employee;
			if (null == command.getId() || command.getId().equals(0))
				employee = new Employee();
			else
				employee = employeeRepository.findById(command.getId());
			
			employee.setEmployeeId(new EmployeeId(command.getEmployeeId()));
			employee.setName(command.getName());
			employee.setGender(command.getGender());
			employee.setBeginDateOfJob(command.getBeginDateOfJob());
			employee.setBeginDateOfWork(command.getBeginDateOfWork());
			employee.setBirthday(command.getBirthday());
			
			if (null == command.getShiftTypeId() || command.getShiftTypeId().equals(0))
				employee.setShiftType(null);
			else {
				ShiftType shiftType = shiftTypeRepository.findById(command.getShiftTypeId());
				if (null != shiftType)
					employee.setShiftType(shiftType);
			}
				
			Unit unit = unitRepository.findById(command.getUnitId());
			// TODO: add job title and job positions
			Job job = new Job(unit, null, null, JobStatus.UNKNOWN, new Date());
			employee.setJob(job);
			
			if (null != command.getProperties()) {
				Set<Preference> preferences = new HashSet<Preference>();
				for(Map.Entry<String, String> property : command.getProperties().entrySet()) {
					preferences.add(new Preference(property.getKey(), property.getValue()));
				}
				employee.setPreferences(preferences);
			}
			
			employeeRepository.save(employee);
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			String[] ids = StringUtils.split(command.getIds(), ActionCommand.ID_SEPARATOR);
			for (String id : ids) {
				Employee employee = employeeRepository.findById(Long.parseLong(id));
				employeeRepository.disable(employee);
			}
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public Employee findById(Long id) {
		return employeeRepository.findById(id);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String queryEmployeesMonthTimeSheetWithJson(QueryTimeSheetCommand command) {
		
		String month = command.getDate();
		TimeSheet.ActionType actionType = command.getActionType();

		List<Date> days = DateHelper.getDaysOfMonth(month);
		TimeSheetDTO ts = new TimeSheetDTO();
		ts.setDays(days);
		
		String[] unitIds = command.getUnitIds().split(",");
		
		if (0 != unitIds.length) {
			List<TimeSheet> records = new ArrayList<TimeSheet>();
			for (String id : unitIds) {
				Long unitId = Long.parseLong(id);
				Unit unit = unitRepository.findById(unitId);
				List<TimeSheet> recs = timeSheetRepository.getMonthTimeSheet(month, unit, actionType);
				
				if (0 == recs.size()) {
					timeSheetRepository.generateMonthTimeSheet(month, unit);
					recs = timeSheetRepository.getMonthTimeSheet(month, unit, actionType);
				}
				records.addAll(recs);
			}
			ts.setRecords(records);
		}
		return JacksonHelper.getTimeSheetJsonWithFilters(ts);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String queryEmployeesDayTimeSheetWithJson(QueryTimeSheetCommand command) {
		
		String day = command.getDate();
		TimeSheet.ActionType actionType = command.getActionType();

		List<Date> days = new ArrayList<Date>();
		days.add(DateHelper.getDate(day));
		TimeSheetDTO ts = new TimeSheetDTO();
		ts.setDays(days);
		
		String[] unitIds = command.getUnitIds().split(",");
		
		if (0 != unitIds.length) {
			List<TimeSheet> records = new ArrayList<TimeSheet>();
			for (String id : unitIds) {
				Long unitId = Long.parseLong(id);
				Unit unit = unitRepository.findById(unitId);
				List<TimeSheet> recs = timeSheetRepository.getDayTimeSheet(day, unit, actionType);
				
				records.addAll(recs);
			}
			ts.setRecords(records);
		}
		return JacksonHelper.getTimeSheetJsonWithFilters(ts);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void generateEmployeesMonthTimeSheet(String month, Long unitId) {
		Unit unit = unitRepository.findById(unitId);
		List<TimeSheet> records = timeSheetRepository.getMonthTimeSheet(month, unit, TimeSheet.ActionType.MONTH_PLAN);
		
		if (0 == records.size()) {
			timeSheetRepository.generateMonthTimeSheet(month, unit);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveTimeSheetRecord(SaveTimeSheetRecordCommand command) {
		Unit unit = unitRepository.findById(command.getUnitId());
		Employee employee = employeeRepository.findById(command.getEmployeeId());
		AttendanceType attendanceType = attendanceTypeRepository.findByName(command.getAttendanceTypeName());
		
		if (command.getCommandType().equals(ActionCommand.ADD)) {
			TimeSheet record;
			if (null == command.getId() || command.getId().equals(0))
				record = new TimeSheet(unit, employee, command.getDate(), command.getBeginTime(), command.getEndTime(), attendanceType, command.getActionType());
			else {
				TimeSheet ts = timeSheetRepository.findById(command.getId());
				ts.setLastActionType(command.getActionType());
				timeSheetRepository.save(ts);
				
				int beginTime = command.getBeginTime() == 0 ? attendanceType.getBeginTime() : command.getBeginTime();
				int endTime = command.getEndTime() == 0 ? attendanceType.getEndTime() : command.getEndTime();
				
				record = new TimeSheet(unit, employee, command.getDate(), beginTime, endTime, attendanceType, command.getActionType());
			}

			timeSheetRepository.save(record);
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)){
			if (null != command.getId() && !command.getId().equals(0)) {
				TimeSheet ts = timeSheetRepository.findById(command.getId());
				ts.setLastActionType(command.getActionType());
				timeSheetRepository.save(ts);
				TimeSheet record = new TimeSheet(ts.getUnit(), ts.getEmployee(), ts.getDate(), ts.getBeginTime(), ts.getEndTime(), ts.getAttendanceType(), command.getActionType());
				timeSheetRepository.disable(record);
			}
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String queryEmployeesActualTimeSheetWithJson(QueryActualTimeSheetCommand command) {
		return JacksonHelper.getJson(timeSheetRepository.queryActualTimeSheet(command));
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String queryEmployeesAbsentTimeSheetWithJson(QueryTimeSheetByPropertyCommand command) {
		List<AttendanceType> ats = attendanceTypeRepository.findByProperty(command.getPropertyName(), "true");
		List<String> atIds = new ArrayList<String>();
		for (AttendanceType at : ats) {
			atIds.add(at.getId().toString());
		}
		command.setattendanceTypeIds(StringUtils.join(atIds, ","));
		
		return JacksonHelper.getJson(timeSheetRepository.queryTimeSheetByProperty(command));
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String queryEmployeesOverTimeTimeSheetWithJson(QueryTimeSheetByPropertyCommand command) {
		List<AttendanceType> ats = attendanceTypeRepository.findByProperty(command.getPropertyName(), "true");
		List<String> atIds = new ArrayList<String>();
		for (AttendanceType at : ats) {
			atIds.add(at.getId().toString());
		}
		command.setattendanceTypeIds(StringUtils.join(atIds, ","));
		
		return JacksonHelper.getJson(timeSheetRepository.queryTimeSheetByProperty(command));
	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void calculateVacation(QueryVacationCommand command) {
		AttendanceType at = attendanceTypeRepository.findByName("年假");
		employeeRepository.calculateVacation(DateHelper.getDate(command.getDate()), at.getId());
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String queryEmployeesVacationWithJson(QueryVacationCommand command) {
		return JacksonHelper.getJson(employeeRepository.queryVacation(command));
	}
	

	
	public void report() {
		List<String> holidays = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getHolidays();

		List<TimeSheet> ts = new ArrayList<TimeSheet>();
		Map<String, Integer> cols = new HashMap<String, Integer>();
		for (TimeSheet r : ts) {
			if (isIncludePreference(r.getAttendanceType(), ReportHelper.REPORT_COLUMN_WORK)) {
				if (cols.containsKey(ReportHelper.REPORT_COLUMN_WORK)) {
					cols.put(ReportHelper.REPORT_COLUMN_WORK, cols.get(ReportHelper.REPORT_COLUMN_WORK) + 1);
				}
				else {
					cols.put(ReportHelper.REPORT_COLUMN_WORK, 1);
				}
				
				if (holidays.contains(DateHelper.getString(r.getDate()))) {
					if (cols.containsKey(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY)) {
						cols.put(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, cols.get(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY) + 1);
					}
					else {
						cols.put(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, 1);
					}
				}
			}
			
			for (String col : ReportHelper.REPORT_COLUMNS_NORMAL.split(",")) {
				if (isIncludePreference(r.getAttendanceType(), col)) {
					if (cols.containsKey(col)) {
						cols.put(col, cols.get(col) + 1);
					}
					else {
						cols.put(col, 1);
					}
				}
			}
			
			if (isIncludePreference(r.getAttendanceType(), ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
				if (isIncludePreference(r.getEmployee(), ReportHelper.REPORT_COLUMN_ALLOWANCE) || isIncludePreference(r.getUnit(), ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
					if (cols.containsKey(ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
						cols.put(ReportHelper.REPORT_COLUMN_ALLOWANCE, cols.get(ReportHelper.REPORT_COLUMN_ALLOWANCE) + 1);
					}
					else {
						cols.put(ReportHelper.REPORT_COLUMN_ALLOWANCE, 1);
					}
				}
			}
		}
	}
	
	private boolean isIncludePreference(PreferenceGetter pg, String key) {
		if (pg.getPreference(key) == null || pg.getPreference(key).equals("false"))
			return false;
		return true;
	}
}
