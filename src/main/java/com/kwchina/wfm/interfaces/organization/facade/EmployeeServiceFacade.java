package com.kwchina.wfm.interfaces.organization.facade;

import java.util.Date;
import java.util.Map;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.interfaces.organization.web.command.ArchiveTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryEmployeeByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveLeaveEventCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SavePreferenceCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveTimeSheetRecordCommand;

public interface EmployeeServiceFacade {

	String queryEmployeesWithJson(QueryCommand command);
	String queryEmployeesByPropertyWithJson(QueryEmployeeByPropertyCommand command);
	void saveEmployee(SaveEmployeeCommand command);
	
	Employee findById(Long id);
	
	String queryEmployeesDayTimeSheetWithJson(QueryTimeSheetCommand command);
	String queryEmployeesMonthTimeSheetWithJson(QueryTimeSheetCommand command);
	
	void generateEmployeesMonthTimeSheet(String date, Long unitId);
	TimeSheet saveTimeSheetRecord(SaveTimeSheetRecordCommand command);
	void saveLeaveEvent(SaveLeaveEventCommand command);
	String queryEmployeesActualTimeSheetWithJson(QueryActualTimeSheetCommand command);
	String queryEmployeesActualTimeSheetWithJson(QueryTimeSheetCommand command);
	
	void calculateVacation(QueryVacationCommand command);
	String queryEmployeesVacationWithJson(QueryVacationCommand command);

	String queryEmployeesAbsentTimeSheetWithJson(QueryTimeSheetByPropertyCommand command);
	String queryEmployeesOverTimeTimeSheetWithJson(QueryTimeSheetByPropertyCommand command);
	
	String getEmployeeShiftType(Long id);
	
	void archiveTimeSheet(ArchiveTimeSheetCommand command);
	void importWorkOrder(Date date, Map<String, String> orders);
	void saveEmployeePreference(SavePreferenceCommand command);
}
