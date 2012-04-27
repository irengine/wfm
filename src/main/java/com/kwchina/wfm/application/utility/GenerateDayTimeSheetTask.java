package com.kwchina.wfm.application.utility;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.kwchina.wfm.infrastructure.common.DateHelper;

public class GenerateDayTimeSheetTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(GenerateDayTimeSheetTask.class);
	
	private String name;
	private WebApplicationContext springContext;
	
	public GenerateDayTimeSheetTask(String name, WebApplicationContext springContext) {
		this.name = name;
		this.springContext = springContext;
	}
	
	@Override
	public void run() {
		logger.info("GenerateDayTimeSheetTask {} executed.", name);
		
		com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade employeeService = springContext.getBean(com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade.class);

        try {
			employeeService.generateDayTimeSheet(DateHelper.getString(new Date()));
		}
        catch(Exception e) {
        	logger.error(e.getMessage());
        	e.printStackTrace();
        }
        finally {
        }
		
	}
}
