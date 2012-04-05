package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceFactory;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;
import com.kwchina.wfm.domain.model.shift.WorkOrder;
import com.kwchina.wfm.domain.model.shift.WorkOrderRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.Page;
import com.kwchina.wfm.interfaces.common.PageHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.common.ReportHelper;
import com.kwchina.wfm.interfaces.organization.dto.TimeSheetDTO;
import com.kwchina.wfm.interfaces.organization.web.command.ActionCommand;
import com.kwchina.wfm.interfaces.organization.web.command.ArchiveTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryEmployeeByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveTimeSheetRecordCommand;
import com.kwchina.wfm.interfaces.report.MonthTimeSheetReport;

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
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeesByPropertyWithJson(QueryEmployeeByPropertyCommand command) {
		List<String> conditions = new ArrayList<String>();
		if (!StringUtils.isEmpty(command.getUnitId())) {
			Unit unit = unitRepository.findById(Long.parseLong(command.getUnitId()));
			String left = String.format("e.job.unit.left >= %d", unit.getLeft());
			conditions.add(left);
			String right = String.format("e.job.unit.right <= %d", unit.getRight());
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
		
		int rowsCount = employeeRepository.getRowsCountBySql(getRowsCountSyntax(whereClause)).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, command.getRows());
		pageHelper.setCurrentPage(command.getPage());
		
		List<Employee> rows = employeeRepository.getRowsBySql(getRowsSyntax(whereClause, orderByClause), pageHelper.getStart(), pageHelper.getPageSize());
		
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		return JacksonHelper.getEmployeeJsonWithFilters(page);
	}
	
	// TODO: refactory preferences query
	private String getRowsSyntax(String whereClause, String orderByClause) {
		return String.format("FROM Employee e, IN(e.preferences) ps WHERE enable=true AND %s %s", whereClause, orderByClause);
	}
	
	private String getRowsCountSyntax(String whereClause) {
		return String.format("SELECT COUNT(*) FROM Employee e, IN(e.preferences) ps WHERE e.enable=true AND %s", whereClause);
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
		MonthTimeSheetReport report = new MonthTimeSheetReport();
		
		String[] unitIds = command.getUnitIds().split(",");
		
		if (0 != unitIds.length) {
			Set<TimeSheet> records = new LinkedHashSet<TimeSheet>();
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
			report.fill(records, days);
		}
		return JacksonHelper.getTimeSheetJsonWithFilters(report);
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
	public TimeSheet saveTimeSheetRecord(SaveTimeSheetRecordCommand command) {
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
				
				int beginTime = command.getBeginTime();
				int endTime = command.getEndTime();
				
				record = new TimeSheet(unit, employee, command.getDate(), beginTime, endTime, attendanceType, command.getActionType());
			}

			timeSheetRepository.save(record);
			return record;
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
		
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	@Deprecated
	public String queryEmployeesActualTimeSheetWithJson(QueryActualTimeSheetCommand command) {
		return JacksonHelper.getJson(timeSheetRepository.queryActualTimeSheet(command));
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String queryEmployeesActualTimeSheetWithJson(QueryTimeSheetCommand command) {
		
		List<String> holidays = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getHolidays();
		String month = command.getDate();

		List<Date> days = DateHelper.getDaysOfMonth(month);
		int dailyShiftCount = shiftTypeRepository.getDailyShiftCount(days);
		MonthTimeSheetReport report = new MonthTimeSheetReport();
		
		String[] unitIds = command.getUnitIds().split(",");
		
		if (0 != unitIds.length) {
			Set<TimeSheet> records = new LinkedHashSet<TimeSheet>();
			for (String id : unitIds) {
				Long unitId = Long.parseLong(id);
				Unit unit = unitRepository.findById(unitId);
				List<TimeSheet> recs = timeSheetRepository.getActualMonthTimeSheet(month, unit);
				
				records.addAll(recs);
			}
			report.fill(records, days, holidays, dailyShiftCount);
		}
		return JacksonHelper.getTimeSheetJsonWithFilters(report);
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
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String getEmployeeShiftType(Long id) {

		Employee employee = employeeRepository.findById(id);
		ShiftType shiftType = null == employee.getShiftType() ? employee.getJob().getUnit().getShiftType() : employee.getShiftType();
		
		return JacksonHelper.getJson(shiftType);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void archiveTimeSheet(ArchiveTimeSheetCommand command) {
		timeSheetRepository.archive(command);
	}
	
	@Autowired
	WorkOrderRepository workOrderRepository;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void importWorkOrder(Date date, Map<String, String> orders) {
		
		List<Employee> list = employeeRepository.findAll();
		
		workOrderRepository.removeAll(date);
		
		for (Employee employee : list) {
			if (ReportHelper.isIncludePreference(employee, ReportHelper.REPORT_COLUMN_SHIFT) || ReportHelper.isIncludePreference(employee.getJob().getUnit(), ReportHelper.REPORT_COLUMN_SHIFT)) {
				
				if (orders.containsKey(employee.getEmployeeId().toString())) {
					WorkOrder workOrder = new WorkOrder(DateHelper.getString(date), employee.getEmployeeId().toString(), orders.get(employee.getEmployeeId().toString()));
					workOrderRepository.save(workOrder);
					timeSheetRepository.importWorkOrder(workOrder);
				}
				else {
					WorkOrder workOrder = new WorkOrder(DateHelper.getString(date), employee.getEmployeeId().toString(), "0");
					workOrderRepository.save(workOrder);
					timeSheetRepository.importWorkOrder(workOrder);
				}
			}
		}
	}

}
