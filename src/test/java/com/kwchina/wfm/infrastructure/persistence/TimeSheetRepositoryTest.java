package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.infrastructure.common.DateHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class TimeSheetRepositoryTest {
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	public void testGetMonthTimeSheet() throws Exception {
		List<TimeSheet> ts = entityManager.createQuery("from TimeSheet ts where ts.unit.id = :unitId and ts.date >= :beginDate and ts.date < :endDate order by ts.employee.employeeId, ts.date")
					.setParameter("unitId", new Long(0))
					.setParameter("beginDate", DateHelper.getDate("2012-02-01"))
					.setParameter("endDate", DateHelper.getDate("2012-03-01"))
					.getResultList();
		
		assertTrue(0 == ts.size());
	}
}
