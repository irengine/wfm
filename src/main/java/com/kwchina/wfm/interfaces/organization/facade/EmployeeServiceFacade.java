package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.organization.Employee;

public interface EmployeeServiceFacade {

	String queryEmployeesWithJson();
	void saveEmployee(Employee employee);
	void saveEmployeeWithUnit(Employee employee, Long unitId);
	
	Employee findById(Long id);
}
