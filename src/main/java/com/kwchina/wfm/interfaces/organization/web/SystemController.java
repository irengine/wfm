package com.kwchina.wfm.interfaces.organization.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.infrastructure.common.HttpHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.facade.SystemServiceFacade;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypeCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveEmployeePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveShiftTypeCommand;

@Controller
public class SystemController {
	
	@Autowired
	SystemServiceFacade systemServiceFacade;
	
	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

	@RequestMapping(value = "/saveHoliday", method = RequestMethod.POST)
	public void saveHoliday(HttpServletResponse response, @ModelAttribute SaveHolidayCommand command) {

		systemServiceFacade.saveHoliday(command);
	}

	@RequestMapping(value = "/getHolidays", method = RequestMethod.GET)
	public void getHolidays(HttpServletRequest request, HttpServletResponse response) {

		int year = 2012;
		if (QueryHelper.isEmpty(request, "year"))
			year = 2012;
		else
			year = Integer.parseInt(request.getParameter("year"));
		
		Map<String, String> days = systemServiceFacade.getHolidays(year);
		
		HttpHelper.output(response, JacksonHelper.getJson(days));
	}

	@RequestMapping(value = "/saveAttendanceTypeProperty", method = RequestMethod.POST)
	public void saveAttendanceTypeProperty(HttpServletResponse response, @ModelAttribute SaveAttendanceTypePropertyCommand command) {

		systemServiceFacade.saveAttendanceTypeProperty(command);
	}

	@RequestMapping(value = "/getAttendanceTypeProperties", method = RequestMethod.GET)
	public void getAttendanceTypeProperties(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryCommand command) {
		logger.info(command.toString());
		
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
		
		HttpHelper.output(response, systemServiceFacade.queryAttendanceTypePropertiesWithJson(parameters, currentPage, pageSize, conditions));
	}

	@RequestMapping(value = "/saveEmployeeProperty", method = RequestMethod.POST)
	public void saveEmployeeProperty(HttpServletResponse response, @ModelAttribute SaveEmployeePropertyCommand command) {

		systemServiceFacade.saveEmployeeProperty(command);
	}

	@RequestMapping(value = "/getEmployeeProperties", method = RequestMethod.GET)
	public void getEmployeeProperties(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryCommand command) {
		logger.info(command.toString());
		
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
		
		HttpHelper.output(response, systemServiceFacade.queryEmployeePropertiesWithJson(parameters, currentPage, pageSize, conditions));
	}

	@RequestMapping(value = "/getAttendanceTypes", method = RequestMethod.GET)
	public void getAttendanceTypes(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryCommand command) {
		
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
		
		HttpHelper.output(response, systemServiceFacade.queryAttendanceTypesWithJson(parameters, currentPage, pageSize, conditions));
	}
	
	@RequestMapping(value = "/getAttendanceType", method = RequestMethod.GET)
	public void getAttendanceType(HttpServletRequest request, HttpServletResponse response) {

		AttendanceType attendanceType;
		if (QueryHelper.isEmpty(request, "id"))
			attendanceType = new AttendanceType();
		else
			attendanceType = systemServiceFacade.findAttendanceTypeById(Long.parseLong(request.getParameter("id")));

		HttpHelper.output(response, JacksonHelper.getJson(attendanceType));
	}
	
	@RequestMapping(value = "/saveAttendanceType", method = RequestMethod.POST)
	public void saveAttendanceType(HttpServletResponse response, @ModelAttribute SaveAttendanceTypeCommand command) {
		systemServiceFacade.saveAttendanceType(command);
	}

	@RequestMapping(value = "/getShiftTypes", method = RequestMethod.GET)
	public void getShiftTypes(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryCommand command) {
		logger.info(command.toString());
//		
//		Map<String, String> parameters = QueryHelper.getQueryParameters(request);
//		
//		int currentPage = 0;
//		if (QueryHelper.isEmpty(request, "page"))
//			currentPage = 0;
//		else
//			currentPage = Integer.parseInt(request.getParameter("page"));
//		
//		int pageSize = 10;
//		if (QueryHelper.isEmpty(request, "rows"))
//			pageSize = 0;
//		else
//			pageSize = Integer.parseInt(request.getParameter("rows"));
//		
//		List<String> conditions = new ArrayList<String>();
//		
//		HttpHelper.output(response, systemServiceFacade.queryShiftTypesWithJson(parameters, currentPage, pageSize, conditions));
		HttpHelper.output(response, systemServiceFacade.queryShiftTypesWithJson(command));
	}
	
	@RequestMapping(value = "/getShiftType", method = RequestMethod.GET)
	public void getShiftType(HttpServletRequest request, HttpServletResponse response) {

		ShiftType shiftType;
		if (QueryHelper.isEmpty(request, "id"))
			shiftType = new ShiftType();
		else
			shiftType = systemServiceFacade.findShiftTypeById(Long.parseLong(request.getParameter("id")));

		HttpHelper.output(response, JacksonHelper.getJson(shiftType));
	}
	
	@RequestMapping(value = "/saveShiftType", method = RequestMethod.POST)
	public void saveShiftType(HttpServletResponse response, @ModelAttribute SaveShiftTypeCommand command) {

		systemServiceFacade.saveShiftType(command);
	}

}
