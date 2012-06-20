package com.kwchina.wfm.infrastructure.persistence;

//import static org.junit.Assert.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.domain.model.employee.TimeSheet.ActionType;
import com.kwchina.wfm.domain.model.employee.TimeSheetRepository;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetCommand;
import com.kwchina.wfm.interfaces.report.MonthTimeSheetReport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class PerformanceTest {
	
	@Autowired
	TimeSheetRepository timeSheetRepository;
	
	@Autowired
	AttendanceTypeRepository attendanceTypeRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	UnitRepository unitRepository;

	@Test
	@Transactional
	public void test() {
		QueryTimeSheetCommand command = new QueryTimeSheetCommand();
		command.setDate("2012-04-01");
		command.setActionType(ActionType.ACTUAL);
		command.setUnitIds("58,142,226,310");
		
		long startTime=System.currentTimeMillis();
		
		String month = command.getDate();
		TimeSheet.ActionType actionType = command.getActionType();

		List<Date> days = DateHelper.getDaysOfMonth(month);
		MonthTimeSheetReport report = new MonthTimeSheetReport();
		
		Set<TimeSheet> records = new LinkedHashSet<TimeSheet>();
		
		List<TimeSheet> recs = timeSheetRepository.getMonthTimeSheet(month, command.getUnitIdList(), actionType);
		records.addAll(recs);
		report.fill(records, days);
		
		System.out.println(JacksonHelper.getTimeSheetJsonWithFilters(report).length());
		
		long endTime=System.currentTimeMillis();
		
		System.out.println(endTime - startTime);
	}
	
	@Test
	@Transactional
	public void testAllQueryTimeSheet() {
		QueryTimeSheetCommand command = new QueryTimeSheetCommand();
		command.setDate("2012-04-01");
		command.setActionType(ActionType.ACTUAL);
		command.setUnitIds("58,142,226,310");
		
		long startTime=System.currentTimeMillis();
		timeSheetRepository.getMonthTimeSheet(command.getDate(), command.getUnitIdList(), command.getActionType());
		timeSheetRepository.getDayTimeSheet(command.getDate(), command.getUnitIdList(), command.getActionType());
		timeSheetRepository.getActualMonthTimeSheet(command.getDate(), command.getUnitIdList());
		long endTime=System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}
	
	@Test
	@Transactional
	public void testQueryMonthTimeSheetByEmployeeIds() {
		QueryTimeSheetCommand command = new QueryTimeSheetCommand();
		command.setDate("2012-04-01");
		command.setActionType(ActionType.ACTUAL);
		command.setUnitIds("58,142,226,310");
		
		long startTime=System.currentTimeMillis();
		
		List<TimeSheet> tss = timeSheetRepository.getMonthTimeSheetByEmployeeIds(command.getDate(), command.getUnitIdList(), command.getActionType());
		System.out.println(tss.size());
		
		long endTime=System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}

}
