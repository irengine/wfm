package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUserCommand;

public interface UserServiceFacade {

	String queryUsersWithJson(QueryCommand command);
	void saveUser(SaveUserCommand command);
	
	User findById(Long id);

}
