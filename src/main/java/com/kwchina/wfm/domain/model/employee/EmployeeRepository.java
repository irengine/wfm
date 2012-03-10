package com.kwchina.wfm.domain.model.employee;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.common.BaseRepository;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;

public interface EmployeeRepository extends BaseRepository<Employee> {

	void disable(Employee employee);
	List<Employee> findByUnitId(Long unitId);
	List<Employee> findAllByUnitId(Long unitId);
	void calculateVacation(Date currentMonth, Long vacationId);
	List<Map<String, Object>> queryVacation(QueryVacationCommand command);
}
