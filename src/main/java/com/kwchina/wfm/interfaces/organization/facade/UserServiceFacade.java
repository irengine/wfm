package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUserCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUserPasswordCommand;

public interface UserServiceFacade {

	String queryUsersWithJson(QueryCommand command);
	void saveUser(SaveUserCommand command);
	
	User findById(Long id);
	void saveUserPassword(SaveUserPasswordCommand command);

}
