package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Transactional
	public void testValidatePassword() {
		User u = new User("xyz", "XuYuZu", "123456", "xyz@kwchina.com");
		assertTrue(u.validatePassword("123456"));
		
		userRepository.save(u);
		assertNotNull(u.getId());
		
		User n = userRepository.findById(u.getId());
		n.validatePassword("123456");
	}

}
