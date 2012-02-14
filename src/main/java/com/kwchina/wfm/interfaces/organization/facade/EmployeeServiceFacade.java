package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.organization.Employee;


public interface EmployeeServiceFacade {

	String queryEmployeesWithJson();
	Employee getEmployee();
	void saveEmployee(Employee employee);
	
	Employee findById(Long id);
}
