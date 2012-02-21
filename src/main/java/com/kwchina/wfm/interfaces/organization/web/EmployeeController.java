package com.kwchina.wfm.interfaces.organization.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;

/**
 * Handles requests for the application home page.
 */
@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeServiceFacade employeeServiceFacade;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	/*
	 * for internet explorer issue, should not use @ResponseBody to return json, instead of use response.write
	 */
	@RequestMapping(value = "/queryEmployees", method = RequestMethod.GET)
	public void queryEmployees(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("get json employees");
		
		Map<String, String> parameters = QueryHelper.getQueryParameters(request);
		
		int currentPage = 0;
		if (QueryHelper.isEmpty(request, "page"))
			currentPage = 0;
		else
			currentPage = Integer.parseInt(request.getParameter("page"));
		
		int pageSize = 10;
		if (QueryHelper.isEmpty(request, "rows"))
			pageSize = 0;
		else
			pageSize = Integer.parseInt(request.getParameter("rows"));
		
		List<String> conditions = new ArrayList<String>();
		if (!QueryHelper.isEmpty(request, "unitId")) {
			String condition = String.format("job.unit.id = %s", request.getParameter("unitId"));
			conditions.add(condition);
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(employeeServiceFacade.queryEmployeesWithJson(parameters, currentPage, pageSize, conditions));
		response.flushBuffer();
	}
	
	@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
	public void getEmployee(HttpServletRequest request,  HttpServletResponse response) throws IOException {
		logger.info("get employee");
		
		Employee employee;
		if (QueryHelper.isEmpty(request, "id"))
			employee = new Employee();
		else
			employee =employeeServiceFacade.findById(Long.parseLong(request.getParameter("id")));
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(JacksonHelper.getJson(employee));
		response.flushBuffer();
	}
	
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public void saveEmployee(@ModelAttribute SaveEmployeeCommand command, HttpServletRequest request, Model model) {
		logger.info("save employee");
		
		employeeServiceFacade.saveEmployee(command);
	}

}
