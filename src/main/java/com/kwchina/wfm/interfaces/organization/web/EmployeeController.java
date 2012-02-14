package com.kwchina.wfm.interfaces.organization.web;

import javax.servlet.http.HttpServletRequest;

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
	public @ResponseBody String queryEmployees() {
		logger.info("get json employees");
		
		return employeeServiceFacade.queryEmployeesWithJson();
	}
	
	@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
	public void getEmployee(HttpServletRequest request, Model model) {
		logger.info("get employee");
		
		String id = request.getParameter("id");
		
		Employee employee;
		
		if (id.isEmpty())
			employee = new Employee();
		else
			employee =employeeServiceFacade.findById(Long.parseLong(id));
		
		model.addAttribute(employee);
	}
	
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public void saveEmployee(@ModelAttribute Employee employee, Model model) {
		logger.info("save employee");
		
		employeeServiceFacade.saveEmployee(employee);
	}

}
