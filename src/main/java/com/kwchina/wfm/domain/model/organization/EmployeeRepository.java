package com.kwchina.wfm.domain.model.organization;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface EmployeeRepository extends BaseRepository<Employee> {

	Employee disable(Employee employee);

}
