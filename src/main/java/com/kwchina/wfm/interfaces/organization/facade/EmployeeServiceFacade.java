package com.kwchina.wfm.interfaces.organization.facade;

import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveTimeSheetRecordCommand;

public interface EmployeeServiceFacade {

	String queryEmployeesWithJson(Map<String, String> parameters, int currentPage, int pageSize, List<String> conditions);
	void saveEmployee(SaveEmployeeCommand command);
	
	Employee findById(Long id);
	
	String queryEmployeesDayTimeSheetWithJson(String date, Long unitId);
	String queryEmployeesMonthTimeSheetWithJson(String month, Long unitId);
	
	void saveTimeSheetRecord(SaveTimeSheetRecordCommand command);
}
