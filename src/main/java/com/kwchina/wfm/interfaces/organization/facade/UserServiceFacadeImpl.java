package com.kwchina.wfm.interfaces.organization.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;

@Component
public class UserServiceFacadeImpl implements UserServiceFacade {

	@Autowired
	UserRepository userRepository;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public User findById(Long id) {
		return userRepository.findById(id);
	}

}
