package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeId;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.employee.Job;
import com.kwchina.wfm.domain.model.employee.JobChangeEvent;
import com.kwchina.wfm.domain.model.employee.JobChangeEventRepository;
import com.kwchina.wfm.domain.model.employee.JobStatus;
import com.kwchina.wfm.domain.model.employee.LeaveEvent;
import com.kwchina.wfm.domain.model.employee.LeaveEventRepository;
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.domain.model.employee.TimeSheetRepository;
import com.kwchina.wfm.domain.model.employee.Vacation;
import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceFactory;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;
import com.kwchina.wfm.domain.model.shift.WorkOrder;
import com.kwchina.wfm.domain.model.shift.WorkOrderRepository;
import com.kwchina.wfm.domain.model.system.SystemAction;
import com.kwchina.wfm.domain.model.system.SystemActionRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.infrastructure.common.PropertiesHelper;
import com.kwchina.wfm.infrastructure.common.SecurityHelper;
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
import com.kwchina.wfm.interfaces.organization.web.command.SaveJobEventCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveLeaveEventCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SavePreferenceCommand;
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
	
	@Autowired
	LeaveEventRepository leaveEventRepository;
	
	@Autowired
	JobChangeEventRepository jobChangeEventRepository;
	
	@Autowired
	SystemActionRepository systemActionRepository;
	
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
		else {
			User user = SecurityHelper.getCurrentUser();
			List<String> unitConditions = new ArrayList<String>();
			for(Unit unit : user.getUnits())
			{
				String unitCondition = String.format("(job.unit.left >= %d and job.unit.right <= %d)", unit.getLeft(), unit.getRight());
				unitConditions.add(unitCondition);
			}
			conditions.add("(" + StringUtils.join(unitConditions, " or ") + ")");
		}
		
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", command.getSort(), command.getOrder());
		
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
		
		String propertyCondition = String.format("((ps1.value = '%s') OR (ps1.value is null AND ps2.key = '%s' AND ps2.value = '%s'))",
						command.getValue(),
						command.getKey(),
						command.getValue());
		conditions.add(propertyCondition);
		
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", command.getSort(), command.getOrder());
		
		if (Boolean.parseBoolean(command.getSearch())) {
			whereClause = QueryHelper.getWhereClause(command.getFilters(), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = employeeRepository.getRowsCountBySql(getRowsCountSyntax(command.getKey(), whereClause)).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, command.getRows());
		pageHelper.setCurrentPage(command.getPage());
		
		List<Employee> rows = employeeRepository.getRowsBySql(getRowsSyntax(command.getKey(), whereClause, orderByClause), pageHelper.getStart(), pageHelper.getPageSize());
		
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		return JacksonHelper.getEmployeeJsonWithFilters(page);
	}
	
	// TODO: refactory preferences query
	private String getRowsSyntax(String propertyName, String whereClause, String orderByClause) {
		return String.format("SELECT DISTINCT e FROM Employee e LEFT OUTER JOIN e.preferences ps1 WITH ps1.key = '%s' LEFT OUTER JOIN e.job.unit.preferences ps2 WHERE e.enable=true AND %s %s",
				propertyName, whereClause, orderByClause);
	}
	
	private String getRowsCountSyntax(String propertyName, String whereClause) {
		return String.format("SELECT COUNT(DISTINCT e) FROM Employee e LEFT OUTER JOIN e.preferences ps1 WITH ps1.key = '%s' LEFT OUTER JOIN e.job.unit.preferences ps2 WHERE e.enable=true AND %s",
				propertyName, whereClause);
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
			/*
			 	if (null != command.getProperties()) {
				Set<Preference> preferences = null == employee.getPreferences() ? new HashSet<Preference>() : employee.getPreferences();
				for(Map.Entry<String, String> property : command.getProperties().entrySet()) {
					Preference p = employee.getPreference(property.getKey());
					if (null != p)
						preferences.remove(p);
					preferences.add(new Preference(property.getKey(), property.getValue()));
				}
				employee.setPreferences(preferences);
			}
			 */
			
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
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveJobEvent(SaveJobEventCommand command) {
		
		Unit unit = unitRepository.findById(command.getUnitId());
		
		String[] ids = StringUtils.split(command.getIds(), ActionCommand.ID_SEPARATOR);
		for (String id : ids) {
			Employee employee = employeeRepository.findById(Long.parseLong(id));

			JobChangeEvent event = new JobChangeEvent(JobChangeEvent.Type.TRANSFER, unit, employee, command.getEffectDate(), null);
			jobChangeEventRepository.addEvent(event);
			
			// TODO: add job title and job positions
			Job job = new Job(unit, null, null, JobStatus.HIRED, new Date());
			employee.setJob(job);
			employeeRepository.save(employee);
		}
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEmployeePreference(SavePreferenceCommand command) {
		
		if (null == command.getId() || command.getId().equals(0))
			return;
		Employee employee = employeeRepository.findById(command.getId());

		Set<Preference> preferences = null == employee.getPreferences() ? new HashSet<Preference>()
				: employee.getPreferences();

		Preference p = employee.getPreference(command.getKey());
		if (null != p)
			preferences.remove(p);
		preferences.add(new Preference(command.getKey(), command.getValue()));
		employee.setPreferences(preferences);
		employeeRepository.save(employee);
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
		
		if (0 != command.getUnitIdList().size()) {
			Set<TimeSheet> records = new LinkedHashSet<TimeSheet>();
			List<TimeSheet> recs = timeSheetRepository.getMonthTimeSheet(month, command.getUnitIdList(), actionType);
			records.addAll(recs);
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
		
		if (0 != command.getUnitIdList().size()) {
			List<TimeSheet> records = new ArrayList<TimeSheet>();
			List<TimeSheet> recs = timeSheetRepository.getDayTimeSheet(day, command.getUnitIdList(), actionType);
			records.addAll(recs);
			ts.setRecords(records);
		}
		return JacksonHelper.getTimeSheetJsonWithFilters(ts);
	}
	
//	@Override
//	@Transactional(propagation=Propagation.REQUIRED)
//	public void generateEmployeesMonthTimeSheet(String month, Long unitId) {
//		Unit unit = unitRepository.findById(unitId);
//		List<TimeSheet> records = timeSheetRepository.getMonthTimeSheet(month, unit, TimeSheet.ActionType.MONTH_PLAN);
//		
//		if (0 == records.size()) {
//			timeSheetRepository.generateMonthTimeSheet(month, unit);
//		}
//	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void generateMonthTimeSheet(String month) {
		
		if( 0 != systemActionRepository.getActions(SystemAction.ScopeType.MONTH_PLAN, month).size())
			return;
		
		systemActionRepository.addAction(SystemAction.ScopeType.MONTH_PLAN, month, null, null);
		timeSheetRepository.generateMonthTimeSheet(month);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void generateDayTimeSheet(String day) {
		
		if( 0 != systemActionRepository.getActions(SystemAction.ScopeType.DAY_PLAN, day).size())
			return;
		
		systemActionRepository.addAction(SystemAction.ScopeType.DAY_PLAN, day, null, null);
		timeSheetRepository.generateDayTimeSheet(day);
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
	public void saveLeaveEvent(SaveLeaveEventCommand command) {

		Unit unit = unitRepository.findById(command.getUnitId());
		Employee employee = employeeRepository.findById(command.getEmployeeId());
		AttendanceType attendanceType = attendanceTypeRepository.findByName(command.getAttendanceTypeName());
		
		if (command.getCommandType().equals(ActionCommand.ADD)) {
			LeaveEvent event = new LeaveEvent(employee, attendanceType, command.getBeginDate(), command.getEndDate());
			leaveEventRepository.save(event);
			
			Calendar c = Calendar.getInstance();
			c.setTime(DateHelper.getFinancialMonth(command.getBeginDate()));
			
			List<Date> days = DateHelper.getDaysOfMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH), command.getBeginDate(), command.getEndDate());
			
			for (Date day : days) {
				List<TimeSheet> tss = timeSheetRepository.getEmployeeDayTimeSheet(day, employee, command.getActionType());
				for (TimeSheet ts : tss) {
					ts.setLastActionType(command.getActionType());
					timeSheetRepository.save(ts);
				}
				
				TimeSheet record = new TimeSheet(unit, employee, day, attendanceType.getBeginTime(), attendanceType.getEndTime(), attendanceType, command.getActionType());
				timeSheetRepository.save(record);
			}

		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)){
			// TODO: delete leave event
		}
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
		
		if (0 != command.getUnitIdList().size()) {
			Set<TimeSheet> records = new LinkedHashSet<TimeSheet>();
			List<TimeSheet> recs = timeSheetRepository.getActualMonthTimeSheet(month, command.getUnitIdList());
			records.addAll(recs);
			report.fill(records, days, holidays, dailyShiftCount);
		}
		return JacksonHelper.getTimeSheetJsonWithFilters(report);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String queryEmployeesAbsentTimeSheetWithJson(QueryTimeSheetByPropertyCommand command) {

		List<String> holidays = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getHolidays();
		int months = DateHelper.monthsBetween(DateHelper.getDate(command.getBeginTime()), DateHelper.getDate(command.getEndTime()));
		
		Map<String, MonthTimeSheetReport> results = new TreeMap<String, MonthTimeSheetReport>();
		for (int i = 0; i < months; i++) {
			String month = DateHelper.getString(DateHelper.addMonth(DateHelper.getDate(command.getBeginTime()), i));
			
			List<Date> days = DateHelper.getDaysOfMonth(month);
			int dailyShiftCount = shiftTypeRepository.getDailyShiftCount(days);
			MonthTimeSheetReport report = new MonthTimeSheetReport();
			
			if (0 != command.getUnitIdList().size()) {
				Set<TimeSheet> records = new LinkedHashSet<TimeSheet>();
				List<TimeSheet> recs = timeSheetRepository.getActualMonthTimeSheet(month, command.getUnitIdList());
				records.addAll(recs);
				report.fill(records, days, holidays, dailyShiftCount);
			}
			
			report.setData(null);
			report.setDays(null);
			results.put(month, report);
		}
		
		return JacksonHelper.getTimeSheetJsonWithFilters(results);
	}
	
//	@Override
//	@Transactional(propagation=Propagation.REQUIRED)
//	public String queryEmployeesOverTimeTimeSheetWithJson(QueryTimeSheetByPropertyCommand command) {
//		List<AttendanceType> ats = attendanceTypeRepository.findByProperty(command.getPropertyName(), "true");
//		List<String> atIds = new ArrayList<String>();
//		for (AttendanceType at : ats) {
//			atIds.add(at.getId().toString());
//		}
//		command.setattendanceTypeIds(StringUtils.join(atIds, ","));
//		
//		return JacksonHelper.getJson(timeSheetRepository.queryTimeSheetByProperty(command));
//	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void calculateVacation(Date currentMonth) {
		AttendanceType at = attendanceTypeRepository.findByName(PropertiesHelper.getProperty(Vacation.Type.ANNUAL_LEAVE.name()));
		employeeRepository.calculateVacation(currentMonth, at.getId());
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void calculateOvertime(Date currentMonth) {
		List<String> holidays = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getHolidays();
		String month = DateHelper.getString(currentMonth);

		List<Date> days = DateHelper.getDaysOfMonth(month);
		int dailyShiftCount = shiftTypeRepository.getDailyShiftCount(days);
		MonthTimeSheetReport report = new MonthTimeSheetReport();
		Set<TimeSheet> records = new LinkedHashSet<TimeSheet>();
		
		Unit unit = unitRepository.findRoot();
		List<Long> unitIdList = new ArrayList<Long>();
		unitIdList.add(unit.getId());
		
		List<TimeSheet> recs = timeSheetRepository.getActualMonthTimeSheet(month, unitIdList);
		records.addAll(recs);
		report.fill(records, days, holidays, dailyShiftCount);
		
		employeeRepository.calculateOvertime(currentMonth, report);
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
		
//		if( 0 != systemActionRepository.getActions(SystemAction.ScopeType.IMPORT, DateHelper.getString(date)).size())
//			return;
		
		systemActionRepository.addAction(SystemAction.ScopeType.IMPORT, DateHelper.getString(date), null, DateTime.now().toString());
		
		List<Employee> list = employeeRepository.findAll();
		
		workOrderRepository.removeAll(date);
		
		for (Employee employee : list) {
			if (ReportHelper.isIncludePreference(employee, ReportHelper.REPORT_COLUMN_IMPORT) || ReportHelper.isIncludePreference(employee.getJob().getUnit(), ReportHelper.REPORT_COLUMN_IMPORT)) {
				
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
