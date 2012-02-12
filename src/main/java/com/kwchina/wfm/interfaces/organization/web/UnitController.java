package com.kwchina.wfm.interfaces.organization.web;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public @ResponseBody String getUnits() throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("get all units");
		
		return unitServiceFacade.getUnitsWithJson();
	}
	
	@RequestMapping(value = "/loadUnits", method = RequestMethod.GET)
	public void loadUnits() throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("load units");
		
		unitServiceFacade.loadSampleData();
	}

}
