package com.kwchina.wfm.domain.model.employee;

import java.util.List;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface EmployeeRepository extends BaseRepository<Employee> {

	Employee disable(Employee employee);
	List<Employee> findByUnitId(Long unitId);
	List<Employee> findAllByUnitId(Long unitId);
}
