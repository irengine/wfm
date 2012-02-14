package com.kwchina.wfm.interfaces.organization.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@RequestMapping(value = "/getUnits", method = RequestMethod.GET)
	public @ResponseBody String getUnits() {
		logger.info("get all units");
		
		return unitServiceFacade.getUnitsWithJson();
	}
	
	@RequestMapping(value = "/queryUnits", method = RequestMethod.GET)
	public @ResponseBody String queryUnits() {
		logger.info("get json units");
		
		return unitServiceFacade.queryUnitsWithJson();
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
	public void saveUnit(@ModelAttribute Unit unit, Model model) {
		logger.info("save employee");
	
		//TODO: make unit
//		unitServiceFacade.createChild(unit);
	}

}
