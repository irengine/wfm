package com.kwchina.wfm.interfaces.organization.facade;

import com.kwchina.wfm.domain.model.organization.User;

public interface UserServiceFacade {

	void saveUser(User user);
	
	User findById(Long id);
}
