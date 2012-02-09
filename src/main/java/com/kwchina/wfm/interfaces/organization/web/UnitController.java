package com.kwchina.wfm.interfaces.organization.web;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kwchina.wfm.domain.model.organization.Unit;

/**
 * Handles requests for the application home page.
 */
@Controller
public class UnitController {
	
	private static final Logger logger = LoggerFactory.getLogger(UnitController.class);
	
	@RequestMapping(value = "/getUnits", method = RequestMethod.GET)
	public @ResponseBody Unit getUnits() {
		logger.info("get all units");

		return new Unit("GIT");
	}

}
