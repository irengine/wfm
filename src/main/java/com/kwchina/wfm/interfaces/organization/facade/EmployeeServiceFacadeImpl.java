package com.kwchina.wfm.interfaces.organization.facade;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Employee;
import com.kwchina.wfm.domain.model.organization.EmployeeRepository;
import com.kwchina.wfm.interfaces.common.Page;
import com.kwchina.wfm.interfaces.common.PageHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;

@Component
public class EmployeeServiceFacadeImpl implements EmployeeServiceFacade {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryEmployeesWithJson() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(QueryHelper.SORT_FIELD, "id");
		parameters.put(QueryHelper.SORT_DIRECTION, "");
		parameters.put(QueryHelper.IS_INCLUDE_CONDITION, "false");
		parameters.put(QueryHelper.FILTERS, "");
		
		String whereClause = QueryHelper.getWhereClause(parameters.get(QueryHelper.FILTERS));
		String orderByClause = String.format(" ORDER BY %s %s ", parameters.get(QueryHelper.SORT_FIELD), parameters.get(QueryHelper.SORT_DIRECTION));
		
		int rowsCount = employeeRepository.getRowsCount(whereClause).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, 5);
		pageHelper.setCurrentPage(0);
		
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

	@Transactional(propagation=Propagation.SUPPORTS)
	public Employee getEmployee() {
		return null;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void saveEmployee(Employee employee) {
		employeeRepository.saveEmployee(employee);
	}

}
