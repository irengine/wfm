package com.kwchina.wfm.interfaces.organization.facade;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.interfaces.common.Page;
import com.kwchina.wfm.interfaces.common.PageHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;

@Component
public class EmployeeServiceFacadeImpl implements EmployeeServiceFacade {

	@Autowired
	UnitRepository unitRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeesWithJson(Map<String, String> parameters, int currentPage, int pageSize, List<String> conditions) {
	
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", parameters.get(QueryHelper.SORT_FIELD), parameters.get(QueryHelper.SORT_DIRECTION));
		
		if (Boolean.parseBoolean(parameters.get(QueryHelper.IS_INCLUDE_CONDITION))) {
			whereClause = QueryHelper.getWhereClause(parameters.get(QueryHelper.FILTERS), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = employeeRepository.getRowsCount(whereClause).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, pageSize);
		pageHelper.setCurrentPage(currentPage);
		
		List<Employee> rows = employeeRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize());
		
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		
		StringWriter sw = new StringWriter();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(sw, page);
		}
		catch(Exception e) {
			
		}
		
		return sw.toString();

	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEmployee(Employee employee) {
		employeeRepository.save(employee);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEmployeeWithUnit(Employee employee, Long unitId) {
		// TODO: remove unit 
//		Unit unit = unitRepository.findById(unitId);
//		employee.setUnit(unit);
		employeeRepository.save(employee);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public Employee findById(Long id) {
		return employeeRepository.findById(id);
	}

}
