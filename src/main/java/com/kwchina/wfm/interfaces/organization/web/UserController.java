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

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.interfaces.organization.facade.UserServiceFacade;

/**
 * Handles requests for the application home page.
 */
@Controller
public class UserController {
	
	@Autowired
	UserServiceFacade userServiceFacade;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public void getUser(HttpServletRequest request, Model model) {
		logger.info("get user");
		
		String id = request.getParameter("id");
		
		User user;
		
		if (StringUtils.isEmpty(id))
			user = new User();
		else
			user =userServiceFacade.findById(Long.parseLong(id));
		
		model.addAttribute(user);
	}
	
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public void saveUser(@ModelAttribute User user, Model model) {
		logger.info("save user");
		
		userServiceFacade.saveUser(user);
	}

}
