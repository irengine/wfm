package com.kwchina.wfm.interfaces.organization.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

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
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.infrastructure.common.HttpHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.dto.EmployeeTimeSheetDTO;
import com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade;
import com.kwchina.wfm.interfaces.organization.web.command.ArchiveTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryEmployeeByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveJobEventCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveLeaveEventCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SavePreferenceCommand;
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
	
	@RequestMapping(value = "/queryEmployeesByProperty", method = RequestMethod.GET)
	public void queryEmployeesByProperty(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryEmployeeByPropertyCommand command) {
		logger.info(command.toString());
		
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesByPropertyWithJson(command));
	}
	
	@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
	public void getEmployee(HttpServletRequest request, HttpServletResponse response) {
		
		Employee employee;
		if (QueryHelper.isEmpty(request, "id"))
			employee = new Employee();
		else
			employee =employeeServiceFacade.findById(Long.parseLong(request.getParameter("id")));
		
		HttpHelper.output(response, JacksonHelper.getEmployeeJsonWithFilters(employee));
	}
	
	@RequestMapping(value = "/getEmployeeShiftType", method = RequestMethod.GET)
	public void getEmployeeShiftType(HttpServletRequest request, HttpServletResponse response) {
		
		Long id = Long.parseLong(request.getParameter("id"));
		HttpHelper.output(response, employeeServiceFacade.getEmployeeShiftType(id));
	}
	
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public void saveEmployee(HttpServletResponse response, @ModelAttribute SaveEmployeeCommand command) {
		
		employeeServiceFacade.saveEmployee(command);
	}
	
	@RequestMapping(value = "/saveJobEvent", method = RequestMethod.POST)
	public void saveJobEvent(HttpServletResponse response, @ModelAttribute SaveJobEventCommand command) {
		
		employeeServiceFacade.saveJobEvent(command);
	}

	@RequestMapping(value = "/saveEmployeePreference", method = RequestMethod.POST)
	public void saveEmployeePreference(HttpServletResponse response, @ModelAttribute SavePreferenceCommand command) {
		
		employeeServiceFacade.saveEmployeePreference(command);
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
	
	@RequestMapping(value = "/querySelectedEmployeesActualTimeSheet", method = RequestMethod.GET)
	public void querySelectedEmployeesActualTimeSheet(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryActualTimeSheetCommand command) {
		
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesActualTimeSheetWithJson(command));
	}

	
	@RequestMapping(value = "/queryEmployeesActualTimeSheet", method = RequestMethod.GET)
	public void queryEmployeesActualTimeSheet(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryTimeSheetCommand command) {
		
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesActualTimeSheetWithJson(command));
	}

	@RequestMapping(value = "/queryEmployeesAbsentTimeSheet", method = RequestMethod.GET)
	public void queryEmployeesAbsentTimeSheet(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryTimeSheetByPropertyCommand command) {
		
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesAbsentTimeSheetWithJson(command));
	}

	@RequestMapping(value = "/queryEmployeesOverTimeSheet", method = RequestMethod.GET)
	public void queryEmployeesOverTimeSheet(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryTimeSheetByPropertyCommand command) {
		
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesAbsentTimeSheetWithJson(command));
	}
	
	@RequestMapping(value = "/generateEmployeesMonthTimeSheet", method = RequestMethod.GET)
	public void generateEmployeesMonthTimeSheet(HttpServletResponse response, @ModelAttribute QueryTimeSheetCommand command) throws IOException {
		
		employeeServiceFacade.generateEmployeesMonthTimeSheet(command.getDate(), command.getUnitId());
	}
	
	@RequestMapping(value = "/saveTimeSheetRecord", method = RequestMethod.POST)
	public TimeSheet saveTimeSheetRecord(HttpServletResponse response, @ModelAttribute SaveTimeSheetRecordCommand command) {

		TimeSheet timesheet = employeeServiceFacade.saveTimeSheetRecord(command);
		
		if (null != timesheet)
			HttpHelper.output(response, JacksonHelper.getEmployeeJsonWithFilters(timesheet));
		
		return timesheet;
	}
	
	@RequestMapping(value = "/saveLeaveEvent", method = RequestMethod.POST)
	public void saveLeaveEvent(HttpServletResponse response, @ModelAttribute SaveLeaveEventCommand command) {

		employeeServiceFacade.saveLeaveEvent(command);
	}
	
	@RequestMapping(value = "/archiveTimeSheet", method = RequestMethod.POST)
	public void archiveTimeSheet(@ModelAttribute ArchiveTimeSheetCommand command) {

		employeeServiceFacade.archiveTimeSheet(command);
	}

	@RequestMapping(value = "/calculateVacation", method = RequestMethod.GET)
	public void calculateVacation(HttpServletResponse response, @ModelAttribute QueryVacationCommand command) {

		employeeServiceFacade.calculateVacation(command);
	}

	@RequestMapping(value = "/queryEmployeesVacation", method = RequestMethod.GET)
	public void queryEmployeesVacation(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryVacationCommand command) {
		
		HttpHelper.output(response, employeeServiceFacade.queryEmployeesVacationWithJson(command));
	}
	
	@RequestMapping(value = "/querySampleEmployeesMonthTimeSheet", method = RequestMethod.GET)
	public void querySampleEmployeesMonthTimeSheet(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryTimeSheetCommand command) {
		List<EmployeeTimeSheetDTO> ts = new ArrayList<EmployeeTimeSheetDTO>();
		List<Date> days = DateHelper.getDaysOfMonth("2012-03-01");
		
		for (int i = 0; i<2000; i++) {
			EmployeeTimeSheetDTO dto = new EmployeeTimeSheetDTO();
			dto.setId(new Long(i));
			dto.setName(String.format("X%dX", i));
			TreeMap<String, String> map = new TreeMap<String, String>(); 
			for (Date day : days) {
				map.put(DateHelper.getString(day), i%2 == 0 ? "日" : "/");
			}
			dto.setValues(map);
			ts.add(dto);
		}

		HttpHelper.output(response, JacksonHelper.getJson(ts));
	}
}
