package com.kwchina.wfm.domain.model.organization;

public interface EmployeeRepository extends BaseRepository<Employee> {

	Employee saveEmployee(Employee employee);
	Employee disableEmployee(Employee employee);

}
