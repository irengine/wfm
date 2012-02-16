package com.kwchina.wfm.interfaces.organization.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.interfaces.organization.facade.UnitServiceFacade;

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
	public void getUnit(HttpServletRequest request, Model model) {
		logger.info("get unit");
		
		String id = request.getParameter("id");
		
		Unit unit;
		
		if (StringUtils.isEmpty(id))
			unit = new Unit();
		else
			unit =unitServiceFacade.findById(Long.parseLong(id));

		model.addAttribute(unit);
	}
	
	@RequestMapping(value = "/saveUnit", method = RequestMethod.POST)
	public void saveUnit(@ModelAttribute Unit unit, HttpServletRequest request, Model model) {
		logger.info("save unit");
	
		if (StringUtils.isEmpty(request.getParameter("parentUnitId"))) {
			unitServiceFacade.saveUnit(unit, null);
		}
		else {
			Long parentUnitId = Long.parseLong(request.getParameter("parentUnitId"));
			unitServiceFacade.saveUnit(unit, parentUnitId);
		}
	}

}
