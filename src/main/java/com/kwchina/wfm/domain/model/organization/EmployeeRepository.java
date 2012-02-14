package com.kwchina.wfm.domain.model.organization;

public interface EmployeeRepository extends BaseRepository<Employee> {

	Employee disable(Employee employee);

}
