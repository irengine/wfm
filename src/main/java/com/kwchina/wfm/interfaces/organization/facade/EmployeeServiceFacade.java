package com.kwchina.wfm.interfaces.organization.facade;

import java.util.Map;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveTimeSheetRecordCommand;

public interface EmployeeServiceFacade {

	String queryEmployeesWithJson(Map<String, String> parameters, int currentPage, int pageSize, String unitId);
	void saveEmployee(SaveEmployeeCommand command);
	
	Employee findById(Long id);
	
	String queryEmployeesDayTimeSheetWithJson(String day, Long unitId);
	String queryEmployeesMonthTimeSheetWithJson(String month, Long unitId);
	
	void generateEmployeesMonthTimeSheet(String date, Long unitId);
	void saveTimeSheetRecord(SaveTimeSheetRecordCommand command);
}
