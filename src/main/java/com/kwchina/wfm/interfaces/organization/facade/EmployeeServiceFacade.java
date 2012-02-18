package com.kwchina.wfm.interfaces.organization.facade;

import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.model.employee.Employee;

public interface EmployeeServiceFacade {

	String queryEmployeesWithJson(Map<String, String> parameters, int currentPage, int pageSize, List<String> conditions);
	void saveEmployee(Employee employee);
	void saveEmployeeWithUnit(Employee employee, Long unitId);
	
	Employee findById(Long id);
}
