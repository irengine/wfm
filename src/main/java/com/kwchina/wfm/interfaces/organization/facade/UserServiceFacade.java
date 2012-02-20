package com.kwchina.wfm.interfaces.organization.facade;

import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUserCommand;

public interface UserServiceFacade {

	void saveUser(SaveUserCommand command);
	
	User findById(Long id);
	String queryUsersWithJson(Map<String, String> parameters, int currentPage, int pageSize, List<String> conditions);

}
