package com.kwchina.wfm.application.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;

public class VacationSnapshotTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(VacationSnapshotTask.class);
	
	private String name;
	private WebApplicationContext springContext;
	
	public VacationSnapshotTask(String name, WebApplicationContext springContext) {
		this.name = name;
		this.springContext = springContext;
	}
	
	@Override
	public void run() {
		logger.info("VacationSnapshotTask {} executed.", name);
		try {
			QueryVacationCommand command = new QueryVacationCommand();
			command.setDate(DateHelper.getString(DateHelper.getMonth()));
			com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade employeeService = springContext.getBean(com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade.class);
			employeeService.calculateVacation(command);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
