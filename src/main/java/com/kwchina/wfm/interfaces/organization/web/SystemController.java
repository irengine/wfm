package com.kwchina.wfm.interfaces.organization.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kwchina.wfm.interfaces.organization.facade.SystemServiceFacade;
import com.kwchina.wfm.interfaces.organization.web.command.SaveHolidayCommand;

@Controller
public class SystemController {
	
	@Autowired
	SystemServiceFacade systemServiceFacade;
	
	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

	@RequestMapping(value = "/saveHoliday", method = RequestMethod.POST)
	public void saveHoliday(@ModelAttribute SaveHolidayCommand command, HttpServletResponse response) throws IOException {
		logger.info("save holiday");
		
		String holidays = systemServiceFacade.saveHoliday(command);
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(holidays);
		response.flushBuffer();
	}

}
