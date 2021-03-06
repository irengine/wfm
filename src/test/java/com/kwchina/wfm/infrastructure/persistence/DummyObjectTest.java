package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class DummyObjectTest {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testSave() throws Exception {
        DummyObject o = new DummyObject();
		entityManager.persist(o);
		assertNotNull(o.getId());
	}
	
	@Test
	@Transactional
//	@ExpectedException(javax.persistence.PersistenceException.class)
	public void testSaveWithException() {
		// TODO: make enable again
//        DummyObject first = new DummyObject("AAA");
//        DummyObject second = new DummyObject("AAA");
//		entityManager.persist(first);
//		entityManager.persist(second);
	}
}
