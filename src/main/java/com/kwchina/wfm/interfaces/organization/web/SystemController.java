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
	public void saveHoliday(@ModelAttribute SaveHolidayCommand command, HttpServletResponse response) {
		logger.info("save holiday");

		try {
			systemServiceFacade.saveHoliday(command);
			HttpHelper.output(response, "1");
		} catch(Exception e) {
			logger.warn(e.getMessage());
			HttpHelper.output(response, "0");
		}
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
	public void saveAttendanceTypeProperty(@ModelAttribute SaveAttendanceTypePropertyCommand command, HttpServletResponse response) {
		logger.info("save holiday");

		try {
			systemServiceFacade.saveAttendanceTypeProperty(command);
			HttpHelper.output(response, "1");
		} catch(Exception e) {
			logger.warn(e.getMessage());
			HttpHelper.output(response, "0");
		}
	}

	@RequestMapping(value = "/getAttendanceTypeProperties", method = RequestMethod.GET)
	public void getAttendanceTypeProperties(@ModelAttribute QueryCommand command, HttpServletRequest request, HttpServletResponse response) {
		logger.info("get all attendanceType propertiess");
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
	public void saveEmployeeProperty(@ModelAttribute SaveEmployeePropertyCommand command, HttpServletResponse response) {
		logger.info("save holiday");

		try {
			systemServiceFacade.saveEmployeeProperty(command);
			HttpHelper.output(response, "1");
		} catch(Exception e) {
			logger.warn(e.getMessage());
			HttpHelper.output(response, "0");
		}
	}

	@RequestMapping(value = "/getEmployeeProperties", method = RequestMethod.GET)
	public void getEmployeeProperties(@ModelAttribute QueryCommand command, HttpServletRequest request, HttpServletResponse response) {
		logger.info("get all employee propertiess");
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
	public void getAttendanceTypes(@ModelAttribute QueryCommand command, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("get all attendanceTypes");
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
		
		HttpHelper.output(response, systemServiceFacade.queryAttendanceTypesWithJson(parameters, currentPage, pageSize, conditions));
	}
	
	@RequestMapping(value = "/getAttendanceType", method = RequestMethod.GET)
	public void getAttendanceType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("get attendanceType");

		AttendanceType attendanceType;
		if (QueryHelper.isEmpty(request, "id"))
			attendanceType = new AttendanceType();
		else
			attendanceType = systemServiceFacade.findAttendanceTypeById(Long.parseLong(request.getParameter("id")));

		HttpHelper.output(response, JacksonHelper.getJson(attendanceType));
	}
	
	@RequestMapping(value = "/saveAttendanceType", method = RequestMethod.POST)
	public void saveAttendanceType(@ModelAttribute SaveAttendanceTypeCommand command, HttpServletResponse response) {
		logger.info("save attendanceType");
		
		try {
			systemServiceFacade.saveAttendanceType(command);
			HttpHelper.output(response, "1");
		} catch(Exception e) {
			logger.warn(e.getMessage());
			HttpHelper.output(response, "0");
		}
	}

	@RequestMapping(value = "/getShiftTypes", method = RequestMethod.GET)
	public void getShiftTypes(@ModelAttribute QueryCommand command, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("get all shiftTypes");
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
		
		HttpHelper.output(response, systemServiceFacade.queryShiftTypesWithJson(parameters, currentPage, pageSize, conditions));
	}
	
	@RequestMapping(value = "/getShiftType", method = RequestMethod.GET)
	public void getShiftType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("get shiftType");

		ShiftType shiftType;
		if (QueryHelper.isEmpty(request, "id"))
			shiftType = new ShiftType();
		else
			shiftType = systemServiceFacade.findShiftTypeById(Long.parseLong(request.getParameter("id")));

		HttpHelper.output(response, JacksonHelper.getJson(shiftType));
	}
	
	@RequestMapping(value = "/saveShiftType", method = RequestMethod.POST)
	public void saveShiftType(@ModelAttribute SaveShiftTypeCommand command, HttpServletResponse response) {
		logger.info("save shiftType");
		
		try {
			systemServiceFacade.saveShiftType(command);
			HttpHelper.output(response, "1");
		} catch(Exception e) {
			logger.warn(e.getMessage());
			HttpHelper.output(response, "0");
		}
	}

}
