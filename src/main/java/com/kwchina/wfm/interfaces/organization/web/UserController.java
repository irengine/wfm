//package com.kwchina.wfm.interfaces.organization.web;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.kwchina.wfm.domain.model.organization.User;
//import com.kwchina.wfm.interfaces.common.JacksonHelper;
//import com.kwchina.wfm.interfaces.common.QueryHelper;
//import com.kwchina.wfm.interfaces.organization.facade.UserServiceFacade;
//import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
//import com.kwchina.wfm.interfaces.organization.web.command.SaveUserCommand;
//
///**
// * Handles requests for the application home page.
// */
//@Controller
//public class UserController {
//	
//	@Autowired
//	UserServiceFacade userServiceFacade;
//	
//	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
//	
//	@RequestMapping(value = "/queryUsers", method = RequestMethod.GET)
//	public void queryUsers(@ModelAttribute QueryCommand command, HttpServletRequest request, HttpServletResponse response) throws IOException {
//		logger.info("get json users");
//		logger.info(command.toString());
//		
//		Map<String, String> parameters = QueryHelper.getQueryParameters(request);
//		
//		int currentPage = 0;
//		if (QueryHelper.isEmpty(request, "page"))
//			currentPage = 0;
//		else
//			currentPage = Integer.parseInt(request.getParameter("page"));
//		
//		int pageSize = 10;
//		if (QueryHelper.isEmpty(request, "rows"))
//			pageSize = 0;
//		else
//			pageSize = Integer.parseInt(request.getParameter("rows"));
//		
//		List<String> conditions = new ArrayList<String>();
//		
//		response.setContentType("text/html;charset=utf-8");
//		response.getWriter().print(userServiceFacade.queryUsersWithJson(parameters, currentPage, pageSize, conditions));
//		response.flushBuffer();
//	}
//	
//	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
//	public void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		logger.info("get user");
//		
//		User user;
//		if (QueryHelper.isEmpty(request, "id"))
//			user = new User();
//		else
//			user =userServiceFacade.findById(Long.parseLong(request.getParameter("id")));
//		
//		response.setContentType("text/html;charset=utf-8");
//		response.getWriter().print(JacksonHelper.getUserJsonWithFilters(user));
//		response.flushBuffer();
//	}
//	
//	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
//	public void saveUser(@ModelAttribute SaveUserCommand command, Model model) {
//		logger.info("save user");
//		
//		userServiceFacade.saveUser(command);
//	}
//
//}
