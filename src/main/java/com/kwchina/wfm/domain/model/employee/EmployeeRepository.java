package com.kwchina.wfm.domain.model.employee;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.common.BaseRepository;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;
import com.kwchina.wfm.interfaces.report.MonthTimeSheetReport;

public interface EmployeeRepository extends BaseRepository<Employee> {

	void disable(Employee employee);
	List<Employee> findByUnitId(Long unitId);
	List<Employee> findAllByUnitId(Long unitId);
	List<Map<String, Object>> queryVacation(QueryVacationCommand command);
	List<Employee> findAll();
	Employee findByCode(String code);

	void calculateVacation(Date currentMonth, Long vacationId);
	void calculateOvertime(Date currentMonth, MonthTimeSheetReport report);
}
