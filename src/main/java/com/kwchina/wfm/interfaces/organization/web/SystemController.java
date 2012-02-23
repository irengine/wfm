package com.kwchina.wfm.interfaces.organization.web;

import java.io.IOException;
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

import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.dto.AttendanceTypePropertyDTO;
import com.kwchina.wfm.interfaces.organization.facade.SystemServiceFacade;
import com.kwchina.wfm.interfaces.organization.web.command.SaveAttendanceTypePropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;

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
			output(response, "1");
		} catch(Exception e) {
			logger.warn(e.getMessage());
			output(response, "0");
		}
	}

	@RequestMapping(value = "/getHolidays", method = RequestMethod.POST)
	public void getHolidays(HttpServletRequest request, HttpServletResponse response) {

		int year = 2012;
		if (QueryHelper.isEmpty(request, "year"))
			year = 2012;
		else
			year = Integer.parseInt(request.getParameter("year"));
		
		Map<String, String> days = systemServiceFacade.getHolidays(year);
		
		output(response, JacksonHelper.getJson(days));
	}

	@RequestMapping(value = "/saveAttendanceTypeProperty", method = RequestMethod.POST)
	public void saveAttendanceTypeProperty(@ModelAttribute SaveAttendanceTypePropertyCommand command, HttpServletResponse response) {
		logger.info("save holiday");

		try {
			systemServiceFacade.saveAttendanceTypeProperty(command);
			output(response, "1");
		} catch(Exception e) {
			logger.warn(e.getMessage());
			output(response, "0");
		}
	}

	@RequestMapping(value = "/getAttendanceTypeProperties", method = RequestMethod.POST)
	public void getAttendanceTypeProperties(HttpServletResponse response) {

		List<AttendanceTypePropertyDTO> properties = systemServiceFacade.getAttendanceTypeProperties();
		
		output(response, JacksonHelper.getJson(properties));
	}
	
	private void output(HttpServletResponse response, String result) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().print(result);
			response.flushBuffer();
		} catch (IOException e) {
		}
	}

}
