package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeId;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.employee.Job;
import com.kwchina.wfm.domain.model.employee.JobStatus;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.CustomShiftPolicy;
import com.kwchina.wfm.domain.model.shift.DailyShiftPolicy;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.Page;
import com.kwchina.wfm.interfaces.common.PageHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.dto.TimeSheetDTO;
import com.kwchina.wfm.interfaces.organization.dto.TimeSheetDTO.TimeSheetRecordDTO;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;

@Component
public class EmployeeServiceFacadeImpl implements EmployeeServiceFacade {

	@Autowired
	UnitRepository unitRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	ShiftTypeRepository shiftTypeRepository;
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeesWithJson(Map<String, String> parameters, int currentPage, int pageSize, List<String> conditions) {
	
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", parameters.get(QueryHelper.SORT_FIELD), parameters.get(QueryHelper.SORT_DIRECTION));
		
		if (Boolean.parseBoolean(parameters.get(QueryHelper.IS_INCLUDE_CONDITION))) {
			whereClause = QueryHelper.getWhereClause(parameters.get(QueryHelper.FILTERS), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = employeeRepository.getRowsCount(whereClause).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, pageSize);
		pageHelper.setCurrentPage(currentPage);
		
		List<Employee> rows = employeeRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize());
		
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		return JacksonHelper.getEmployeeJsonWithFilters(page);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEmployee(SaveEmployeeCommand command) {
		
		Employee employee;
		if (null == command.getId() || command.getId().equals(0))
			employee = new Employee();
		else
			employee = employeeRepository.findById(command.getId());
		
		employee.setEmployeeId(new EmployeeId(command.getEmployeeId()));
		employee.setName(command.getName());
		employee.setBeginDateOfJob(command.getBeginDateOfJob());
		employee.setBeginDateOfWork(command.getBeginDateOfWork());
		employee.setBirthday(command.getBirthday());
		
		ShiftType shiftType = shiftTypeRepository.findById(command.getShiftTypeId());
		if (null != shiftType)
			employee.setShiftType(shiftType);
			
		Unit unit = unitRepository.findById(command.getUnitId());
		// TODO: add job title and job positions
		Job job = new Job(unit, null, null, JobStatus.UNKNOWN, new Date());
		employee.setJob(job);
		employeeRepository.save(employee);
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public Employee findById(Long id) {
		return employeeRepository.findById(id);
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeesMonthTimeSheetWithJson(String month, Long unitId) {
		List<Date> days = DateHelper.getDaysOfMonth(month);
		Unit unit = unitRepository.findById(unitId);
		ShiftType defaultShiftType = unit.getShiftType();
		
		// TODO: refactory
		TimeSheetDTO ts = new TimeSheetDTO();
		ts.setDays(days);
		List<TimeSheetRecordDTO> records = new ArrayList<TimeSheetRecordDTO>();

		List<Employee> employees = employeeRepository.findByUnitId(unitId);
		for(Employee employee : employees) {
			
			Map<Date, AttendanceType> dayAttendances = new HashMap<Date, AttendanceType>();
			ShiftType shiftType = null == employee.getShiftType() ? defaultShiftType : employee.getShiftType();
			
			if ("CustomShiftPolicy" == shiftType.getStrategyClassName()) {
				CustomShiftPolicy csp = shiftTypeRepository.getCustomShiftPolicy(shiftType.getStrategyClassParameters());
				
				for(Date day : days) {
					dayAttendances.put(day, csp.getAttendanceType(day));
				}
				
			}
			else if ("DailyShiftPolicy" == shiftType.getStrategyClassName()) {
				DailyShiftPolicy dsp = shiftTypeRepository.getDailyShiftPolicy(shiftType.getStrategyClassParameters());
				
				for(Date day : days) {
					dayAttendances.put(day, dsp.getAttendanceType(day));
				}
			}

			TimeSheetRecordDTO tsr = ts.new TimeSheetRecordDTO();
			tsr.setEmployee(employee);
			tsr.setDayAttendances(dayAttendances);
			records.add(tsr);
		}
		
		ts.setRecords(records);
		
		return JacksonHelper.getJson(ts);
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeesDayTimeSheetWithJson(String date, Long unitId) {
		List<Date> days = new ArrayList<Date>();
		days.add(DateHelper.getDate(date));
		
		Unit unit = unitRepository.findById(unitId);
		ShiftType defaultShiftType = unit.getShiftType();
		
		// TODO: refactory
		TimeSheetDTO ts = new TimeSheetDTO();
		ts.setDays(days);
		List<TimeSheetRecordDTO> records = new ArrayList<TimeSheetRecordDTO>();

		List<Employee> employees = employeeRepository.findByUnitId(unitId);
		for(Employee employee : employees) {
			
			Map<Date, AttendanceType> dayAttendances = new HashMap<Date, AttendanceType>();
			ShiftType shiftType = null == employee.getShiftType() ? defaultShiftType : employee.getShiftType();
			
			if ("CustomShiftPolicy" == shiftType.getStrategyClassName()) {
				CustomShiftPolicy csp = shiftTypeRepository.getCustomShiftPolicy(shiftType.getStrategyClassParameters());
				
				for(Date day : days) {
					dayAttendances.put(day, csp.getAttendanceType(day));
				}
				
			}
			else if ("DailyShiftPolicy" == shiftType.getStrategyClassName()) {
				DailyShiftPolicy dsp = shiftTypeRepository.getDailyShiftPolicy(shiftType.getStrategyClassParameters());
				
				for(Date day : days) {
					dayAttendances.put(day, dsp.getAttendanceType(day));
				}
			}

			TimeSheetRecordDTO tsr = ts.new TimeSheetRecordDTO();
			tsr.setEmployee(employee);
			tsr.setDayAttendances(dayAttendances);
			records.add(tsr);
		}
		
		ts.setRecords(records);
		
		return JacksonHelper.getJson(ts);
	}

}
