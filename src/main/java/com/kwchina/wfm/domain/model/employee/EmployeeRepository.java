package com.kwchina.wfm.domain.model.employee;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface EmployeeRepository extends BaseRepository<Employee> {

	Employee disable(Employee employee);

}
