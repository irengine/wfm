package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UnitRepository unitRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testValidatePassword() {
		User u = new User("xyz", "XuYuZu", "xyz@kwchina.com");
		u.setPassword("123456");
		assertTrue(u.validatePassword("123456"));
		
		userRepository.save(u);
		assertNotNull(u.getId());
		
		User n = userRepository.findById(u.getId());
		n.validatePassword("123456");
	}
	
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

}
