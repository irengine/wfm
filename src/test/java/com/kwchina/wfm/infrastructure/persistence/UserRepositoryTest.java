package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;
import com.kwchina.wfm.infrastructure.common.ApplicationContextProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
//	
//	@Autowired
//	private UnitRepository unitRepository;
//	
//	@PersistenceContext
//	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testValidatePassword() {
		User u = new User("sysAdmin", "System Administrator", "sa@kwchina.com");
		u.setPassword("P@ssword");
		assertTrue(u.validatePassword("P@ssword"));
		
		userRepository.save(u);
		assertNotNull(u.getId());
		
		User n = userRepository.findById(u.getId());
		n.validatePassword("P@ssword");
	}
	
	@Test
	@Transactional
	public void testGetUserName() {
		ApplicationContext context = ApplicationContextProvider.getApplicationContext();
		UserRepository ur = context.getBean(com.kwchina.wfm.domain.model.organization.UserRepository.class);
		User u = ur.findByCode("sysAdmin");
		assertNotNull(u);
	}
	
	// TODO: add more unit test
	/*
	@Test
	@Transactional
	public void testUserForUnit() {
		Unit x = new Unit("X");
		Unit xx = new Unit("XX");
		
		entityManager.persist(x);
		entityManager.persist(xx);
		
		User u = new User("xyz", "XuYuZu", "xyz@kwchina.com");
		u.setPassword("123456");

		u.getUnits().add(x);
		u.getUnits().add(xx);
		
		entityManager.persist(u);
		entityManager.flush();
		
		User v = userRepository.findById(u.getId());
		assertEquals(2, v.getUnits().size());
		
		Unit y = unitRepository.findById(x.getId());
		assertEquals(1, y.getUsers().size());
		Unit yy = unitRepository.findById(xx.getId());
		assertEquals(1, yy.getUsers().size());
	}
	
	@Test
	@Transactional
	public void testUnitForUser() {
		
		User u = new User("xyz", "XuYuZu", "xyz@kwchina.com");
		u.setPassword("123456");
		
		entityManager.persist(u);
		
		User uu = new User("xyz", "XuYuZu", "xyz@kwchina.com");
		uu.setPassword("123456");
		
		entityManager.persist(uu);

		Unit x = new Unit("X");
		
		x.getUsers().add(u);
		x.getUsers().add(uu);
		
		entityManager.persist(x);
		
		Unit y = unitRepository.findById(x.getId());
		assertEquals(2, y.getUsers().size());
		
		User v = userRepository.findById(u.getId());
		assertEquals(1, v.getUnits().size());
	}
	*/
}
