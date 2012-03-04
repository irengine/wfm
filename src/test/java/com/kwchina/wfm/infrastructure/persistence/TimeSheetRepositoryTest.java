package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeId;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.domain.model.employee.TimeSheet.ActionType;
import com.kwchina.wfm.domain.model.employee.TimeSheetRepository;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class TimeSheetRepositoryTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	TimeSheetRepository timeSheetRepository;
	
	@Autowired
	AttendanceTypeRepository attendanceTypeRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	UnitRepository unitRepository;

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
	
	@Test
	public void testActionTypeCompare() {
		TimeSheet.ActionType a1 = TimeSheet.ActionType.MONTH_PLAN;
		TimeSheet.ActionType a2 = TimeSheet.ActionType.MONTH_PLAN_ADJUST;
		assertFalse(a1 == a2);
	}
	
	@Test
	public void testRemoveChildFromCollection() {
		Set<String> ss = new HashSet<String>();
		ss.add("XX1");
		ss.add("XX1");
		ss.add("XX2");
		System.out.println(ss.size());
		assertTrue(ss.size() == 2);
		
		ss.remove("XX1");
		System.out.println(ss.size());
		assertTrue(ss.size() == 1);
		
		
		List<String> ls = new ArrayList<String>();
		ls.add("XX1");
		ls.add("XX1");
		ls.add("XX2");
		System.out.println(ls.size());
		assertTrue(ls.size() == 3);
		
		ls.remove("XX1");
		System.out.println(ls.size());
		assertTrue(ls.size() == 2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(false)
	public void testSaveTimeSheet() {
		Date date = DateHelper.getDate("2012-02-14");
		Unit u = new Unit("X");
		unitRepository.save(u);
		
		Employee e = new Employee(new EmployeeId("0001"), "Alex Tang", date, date, date);
		employeeRepository.save(e);
		
		AttendanceType at = new AttendanceType("Day", 8, 16);
		attendanceTypeRepository.save(at);
		
		TimeSheet ts1 = new TimeSheet(u, e, date, 8, 16, at, ActionType.MONTH_PLAN);
		timeSheetRepository.save(ts1);
		
		TimeSheet ts2 = new TimeSheet(u, e, date, 8, 12, at, ActionType.MONTH_PLAN_ADJUST);
		ts2.setReferTo(ts1);
		timeSheetRepository.save(ts2);
		
		List<TimeSheet> tss1 = entityManager.createQuery("from TimeSheet ts where ts.actionType <= :actionType order by ts.employee.employeeId, ts.date, ts.actionType, ts.updatedAt")
				.setParameter("actionType", TimeSheet.ActionType.MONTH_PLAN)
				.getResultList();

		assertTrue(1 == tss1.size());

		List<TimeSheet> tss2 = entityManager.createQuery("from TimeSheet ts where ts.actionType <= :actionType order by ts.employee.employeeId, ts.date, ts.actionType, ts.updatedAt")
				.setParameter("actionType", TimeSheet.ActionType.MONTH_PLAN_ADJUST)
				.getResultList();

		assertTrue(2 == tss2.size());
		
		List<TimeSheet> qs1 = timeSheetRepository.getMonthTimeSheet("2012-02-14", u);
		assertTrue(1 == qs1.size());
		assertEquals(TimeSheet.ActionType.MONTH_PLAN_ADJUST, qs1.get(0).getActionType());
		
		ts2.setEnable(false);
		timeSheetRepository.save(ts2);
		
		List<TimeSheet> qs2 = timeSheetRepository.getMonthTimeSheet("2012-02-14", u);
		assertTrue(0 == qs2.size());
	}
}
