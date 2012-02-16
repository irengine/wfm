package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.User;

/*
 * Sample test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
@TransactionConfiguration(defaultRollback=false)
@Transactional
public class JPATests {
	
	private static final Logger logger = LoggerFactory.getLogger(DummyObjectTests.class);

	@PersistenceContext
	private EntityManager entityManager;
	
    @BeforeTransaction
    public void verifyInitialDatabaseState() {
        logger.info("logic to verify the initial state before a transaction is started");
    }

    @Before
    public void setUpTestDataWithinTransaction() {
    	logger.info("set up test data within the transaction");
    }

    /*
        MyEntity e = new MyEntity() {}; 
    	 
    	// scenario 1 
    	// tran starts 
    	em.persist(e);  
    	e.setSomeField(someValue);  
    	// tran ends, and the row for someField is updated in the database 
    	 
    	// scenario 2 
    	// tran starts 
    	e = new MyEntity(); 
    	em.merge(e); 
    	e.setSomeField(anotherValue);  
    	// tran ends but the row for someField is not updated in the database (you made the changes *after* merging 
    	 
    	// scenario 3 
    	// tran starts 
    	e = new MyEntity(); 
    	MyEntity e2 = em.merge(e); 
    	e2.setSomeField(anotherValue);  
    	// tran ends and the row for someField is updated (the changes were made to e2, not e)

     */
    @Test
    @Rollback(true)
    public void testMerge() {
    	Unit unit = new Unit("Save");
    	entityManager.persist(unit);

    	Unit mergeUnit = new Unit();
    	mergeUnit.setId(unit.getId());
    	mergeUnit.setName("Merge");
    	mergeUnit.setLeft(new Long(100));
    	entityManager.merge(mergeUnit);
    	
    	assertEquals(new Long(100), unit.getLeft());
    }

//    @Test
//    // overrides the class-level defaultRollback setting
//    @Rollback(true)
//    public void modifyDatabaseWithinTransaction() {
//    	logger.info("logic which uses the test data and modifies database state");
//    }
    
    @Test
    @Rollback(true)
    public void testElementCollections() {
    	User u = new User("aka", "Alex Tang", "alex.tang@taoware.com");
    	
    	Set<Preference> ps = new HashSet<Preference>();
    	Preference p = new Preference();
    	p.setKey("K");
    	p.setValue("V");
    	ps.add(p);
    	
    	u.setPassword("123456");
    	u.setPreferences(ps);
    	
    	entityManager.persist(u);
    }

    @After
    public void tearDownWithinTransaction() {
    	logger.info("execute \"tear down\" logic within the transaction");
    }

    @AfterTransaction
    public void verifyFinalDatabaseState() {
    	logger.info("logic to verify the final state after transaction has rolled back");
    }
}
