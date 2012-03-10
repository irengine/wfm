package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.interfaces.organization.web.command.QueryAbsentTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveTimeSheetRecordCommand;

public interface EmployeeServiceFacade {

	String queryEmployeesWithJson(QueryCommand command);
	void saveEmployee(SaveEmployeeCommand command);
	
	Employee findById(Long id);
	
	String queryEmployeesDayTimeSheetWithJson(QueryTimeSheetCommand command);
	String queryEmployeesMonthTimeSheetWithJson(QueryTimeSheetCommand command);
	
	void generateEmployeesMonthTimeSheet(String date, Long unitId);
	void saveTimeSheetRecord(SaveTimeSheetRecordCommand command);
	String queryEmployeesActualTimeSheetWithJson(QueryActualTimeSheetCommand command);
	String queryEmployeesAbsentTimeSheetWithJson(QueryAbsentTimeSheetCommand command);
	
	void calculateVacation(QueryVacationCommand command);
}
