package com.kwchina.wfm.interfaces.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.infrastructure.common.HttpHelper;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.facade.UnitServiceFacade;
import com.kwchina.wfm.interfaces.organization.facade.UserServiceFacade;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUnitCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUserCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUserPasswordCommand;

/**
 * Handles requests for the application home page.
 */
@Controller
public class UnitController {
	
	@Autowired
	UnitServiceFacade unitServiceFacade;
	
	@Autowired
	UserServiceFacade userServiceFacade;
	
	private static final Logger logger = LoggerFactory.getLogger(UnitController.class);

	/*
	 * for test only
	 */
	@RequestMapping(value = "/loadUnits", method = RequestMethod.GET)
	public void loadUnits(HttpServletResponse response) {
		logger.info("load units");
		
		unitServiceFacade.loadSampleData();
		
		HttpHelper.output(response, "load sample units done.");
	}
	
	/*
	 * for internet explorer issue, should not use @ResponseBody to return json, instead of use response.write
	 */
	@RequestMapping(value = "/getUnits", method = RequestMethod.GET)
	public void getUnits(HttpServletRequest request, HttpServletResponse response) {
	
		HttpHelper.output(response, unitServiceFacade.getUnitsWithJson());
	}
	
	@RequestMapping(value = "/getUnit", method = RequestMethod.GET)
	public void getUnit(HttpServletRequest request, HttpServletResponse response) {

		Unit unit;
		if (QueryHelper.isEmpty(request, "id"))
			unit = new Unit();
		else
			unit =unitServiceFacade.findById(Long.parseLong(request.getParameter("id")));

		HttpHelper.output(response, JacksonHelper.getUnitJsonWithFilters(unit));
	}
	
	@RequestMapping(value = "/saveUnit", method = RequestMethod.POST)
	public void saveUnit(HttpServletResponse response, @ModelAttribute SaveUnitCommand command) {
		
		unitServiceFacade.saveUnit(command);
	}
	
	/*
	 * User
	 */
	@RequestMapping(value = "/queryUsers", method = RequestMethod.GET)
	public void queryUsers(HttpServletRequest request, HttpServletResponse response, @ModelAttribute QueryCommand command){
		logger.info(command.toString());

		HttpHelper.output(response, userServiceFacade.queryUsersWithJson(command));
	}
	
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public void getUser(HttpServletRequest request, HttpServletResponse response) {
		
		User user;
		if (QueryHelper.isEmpty(request, "id"))
			user = new User();
		else
			user =userServiceFacade.findById(Long.parseLong(request.getParameter("id")));
		
		HttpHelper.output(response, JacksonHelper.getUserJsonWithFilters(user));
	}
	
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public void saveUser(HttpServletResponse response, @ModelAttribute SaveUserCommand command) {
		
		userServiceFacade.saveUser(command);
	}
	
	@RequestMapping(value = "/saveUserPassword", method = RequestMethod.POST)
	public void saveUserPassword(HttpServletResponse response, @ModelAttribute SaveUserPasswordCommand command) {
		
		userServiceFacade.saveUserPassword(command);
	}
}
