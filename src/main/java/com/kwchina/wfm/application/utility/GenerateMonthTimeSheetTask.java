package com.kwchina.wfm.application.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.kwchina.wfm.infrastructure.common.DateHelper;

public class GenerateMonthTimeSheetTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(GenerateMonthTimeSheetTask.class);
	
	private String name;
	private WebApplicationContext springContext;
	
	public GenerateMonthTimeSheetTask(String name, WebApplicationContext springContext) {
		this.name = name;
		this.springContext = springContext;
	}
	
	@Override
	public void run() {
		logger.info("GenerateMonthTimeSheetTask {} executed.", name);
		
		com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade employeeService = springContext.getBean(com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade.class);

        try {
			employeeService.generateMonthTimeSheet(DateHelper.getString(DateHelper.getMonth()));
		}
        catch(Exception e) {
        	logger.error(e.getMessage());
        	e.printStackTrace();
        }
        finally {
        }
		
	}
}
