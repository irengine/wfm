package com.kwchina.wfm.interfaces.organization.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.infrastructure.common.HttpHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveTimeSheetRecordCommand;

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
	public void queryEmployees(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryCommand command) {
		logger.info(command.toString());
		
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesWithJson(command));
	}
	
	@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
	public void getEmployee(HttpServletRequest request, HttpServletResponse response) {
		
		Employee employee;
		if (QueryHelper.isEmpty(request, "id"))
			employee = new Employee();
		else
			employee =employeeServiceFacade.findById(Long.parseLong(request.getParameter("id")));
		
		HttpHelper.output(response, JacksonHelper.getUserJsonWithFilters(employee));
	}
	
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public void saveEmployee(HttpServletResponse response, @ModelAttribute SaveEmployeeCommand command) {
		
		employeeServiceFacade.saveEmployee(command);
	}

	
	@RequestMapping(value = "/queryEmployeesDayTimeSheet", method = RequestMethod.GET)
	public void queryEmployeesDayTimeSheet(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryTimeSheetCommand command) {

		if (null == command.getActionType())
			command.setActionType(TimeSheet.ActionType.DAY_PLAN_ADJUST);
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesDayTimeSheetWithJson(command));
	}

	@RequestMapping(value = "/queryEmployeesMonthTimeSheet", method = RequestMethod.GET)
	public void queryEmployeesMonthTimeSheet(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryTimeSheetCommand command) {

		if (null == command.getActionType())
			command.setActionType(TimeSheet.ActionType.MONTH_PLAN_ADJUST);
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesMonthTimeSheetWithJson(command));
	}
	
	@RequestMapping(value = "/queryEmployeesActualTimeSheet", method = RequestMethod.GET)
	public void queryEmployeesActualTimeSheet(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryActualTimeSheetCommand command) {
		
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesActualTimeSheetWithJson(command));
	}
	
	@RequestMapping(value = "/generateEmployeesMonthTimeSheet", method = RequestMethod.GET)
	public void generateEmployeesMonthTimeSheet(HttpServletResponse response, @ModelAttribute QueryTimeSheetCommand command) throws IOException {
		
		employeeServiceFacade.generateEmployeesMonthTimeSheet(command.getDate(), command.getUnitId());
	}
	
	@RequestMapping(value = "/saveTimeSheetRecored", method = RequestMethod.POST)
	public void saveTimeSheetRecored(HttpServletResponse response, @ModelAttribute SaveTimeSheetRecordCommand command) {

		employeeServiceFacade.saveTimeSheetRecord(command);
	}

}
