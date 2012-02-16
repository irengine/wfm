package com.kwchina.wfm.interfaces.organization.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kwchina.wfm.domain.model.organization.Employee;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade;

/**
 * Handles requests for the application home page.
 */
@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeServiceFacade employeeServiceFacade;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@RequestMapping(value = "/queryEmployees", method = RequestMethod.GET)
	public @ResponseBody String queryEmployees(HttpServletRequest request) {
		logger.info("get json employees");
		
		Map<String, String> parameters = QueryHelper.getQueryParameters(request);
		
		int currentPage = 0;
		if (StringUtils.isEmpty(request.getParameter("page")))
			currentPage = 0;
		else
			currentPage = Integer.parseInt(request.getParameter("page"));
		
		int pageSize = 10;
		if (StringUtils.isEmpty(request.getParameter("rows")))
			pageSize = 0;
		else
			pageSize = Integer.parseInt(request.getParameter("rows"));
		
		List<String> conditions = new ArrayList<String>();
		if (!StringUtils.isEmpty(request.getParameter("unitId"))) {
			String condition = String.format("unit.id = %s", request.getParameter("unitId"));
			conditions.add(condition);
		}
		
		return employeeServiceFacade.queryEmployeesWithJson(parameters, currentPage, pageSize, conditions);
	}
	
	@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
	public void getEmployee(HttpServletRequest request, Model model) {
		logger.info("get employee");
		
		String id = request.getParameter("id");
		
		Employee employee;
		
		if (StringUtils.isEmpty(id))
			employee = new Employee();
		else
			employee =employeeServiceFacade.findById(Long.parseLong(id));
		
		model.addAttribute(employee);
	}
	
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public void saveEmployee(@ModelAttribute Employee employee, HttpServletRequest request, Model model) {
		logger.info("save employee");
		
		if (StringUtils.isEmpty(request.getParameter("unitId"))) {
			employeeServiceFacade.saveEmployee(employee);
		}
		else {
			Long unitId = Long.parseLong(request.getParameter("unitId"));
			employeeServiceFacade.saveEmployeeWithUnit(employee, unitId);
		}
	}

}
