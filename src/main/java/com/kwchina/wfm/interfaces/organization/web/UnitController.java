package com.kwchina.wfm.interfaces.organization.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.facade.UnitServiceFacade;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUnitCommand;

/**
 * Handles requests for the application home page.
 */
@Controller
public class UnitController {
	
	@Autowired
	UnitServiceFacade unitServiceFacade;
	
	private static final Logger logger = LoggerFactory.getLogger(UnitController.class);
	
	/*
	 * for internet explorer issue, should not use @ResponseBody to return json, instead of use response.write
	 */
	@RequestMapping(value = "/getUnits", method = RequestMethod.GET)
	public void getUnits(HttpServletResponse response) throws IOException {
		logger.info("get all units");
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(unitServiceFacade.getUnitsWithJson());
		response.flushBuffer();
	}
	
	@RequestMapping(value = "/loadUnits", method = RequestMethod.GET)
	public void loadUnits() {
		logger.info("load units");
		
		unitServiceFacade.loadSampleData();
	}
	
	@RequestMapping(value = "/getUnit", method = RequestMethod.GET)
	public void getUnit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("get unit");

		Unit unit;
		if (QueryHelper.isEmpty(request, "id"))
			unit = new Unit();
		else
			unit =unitServiceFacade.findById(Long.parseLong(request.getParameter("id")));

		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(JacksonHelper.getUnitJsonWithFilters(unit));
		response.flushBuffer();
	}
	
	@RequestMapping(value = "/saveUnit", method = RequestMethod.POST)
	public void saveUnit(@ModelAttribute SaveUnitCommand command, HttpServletRequest request, Model model) {
		logger.info("save unit");
		
		unitServiceFacade.saveUnit(command);
	}

}
