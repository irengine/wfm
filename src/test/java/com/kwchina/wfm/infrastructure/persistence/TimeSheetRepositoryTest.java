package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeId;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.employee.Job;
import com.kwchina.wfm.domain.model.employee.JobPosition;
import com.kwchina.wfm.domain.model.employee.JobStatus;
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.domain.model.employee.Employee.Gender;
import com.kwchina.wfm.domain.model.employee.TimeSheet.ActionType;
import com.kwchina.wfm.domain.model.employee.TimeSheetRepository;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class TimeSheetRepositoryTest {
	
	@Test
	public void testActionTypeCompare() {
		TimeSheet.ActionType a1 = TimeSheet.ActionType.MONTH_PLAN;
		TimeSheet.ActionType a2 = TimeSheet.ActionType.MONTH_PLAN_ADJUST;
		assertFalse(a1 == a2);
	}
	
	@Test
	public void testDifferenceBetweenSetAndList() {
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
	public void testIsGetMonthTimeSheetSQLWorking() throws Exception {
		List<TimeSheet> ts = entityManager.createQuery("from TimeSheet ts where ts.unit.id = :unitId and ts.date >= :beginDate and ts.date < :endDate order by ts.employee.employeeId, ts.date")
					.setParameter("unitId", new Long(0))
					.setParameter("beginDate", DateHelper.getDate("2012-02-01"))
					.setParameter("endDate", DateHelper.getDate("2012-03-01"))
					.getResultList();
		
		assertTrue(0 == ts.size());
	}

	@Test
	@Transactional
	public void testCreateTimeSheet() {
		String day = "2012-02-14";
		Date date = DateHelper.getDate(day);
		Unit unit = unitRepository.getRoot("XX");
		
		Employee e = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);
		employeeRepository.save(e);
		
		AttendanceType at = new AttendanceType("Day", 8, 16);
		attendanceTypeRepository.save(at);
		
		TimeSheet ts1 = new TimeSheet(unit, e, date, 8, 16, at, ActionType.MONTH_PLAN);
		timeSheetRepository.save(ts1);
		
		List<TimeSheet> monthTs = timeSheetRepository.getMonthTimeSheet(day, unit, ActionType.MONTH_PLAN);
		assertTrue( 1 == monthTs.size());

		List<TimeSheet> dayTs = timeSheetRepository.getDayTimeSheet(day, unit, ActionType.MONTH_PLAN);
		assertTrue( 1 == dayTs.size());
	}
	
	@Test
	@Transactional
	public void testUpdateTimeSheet() {
		String day = "2012-02-14";
		Date date = DateHelper.getDate(day);
		Unit unit = unitRepository.getRoot("XX");
		
		Employee e = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);
		employeeRepository.save(e);
		
		AttendanceType at = new AttendanceType("Day", 8, 16);
		attendanceTypeRepository.save(at);
		
		// Create 2 instance
		TimeSheet ts11 = new TimeSheet(unit, e, date, 8, 16, at, ActionType.MONTH_PLAN);
		timeSheetRepository.save(ts11);

		TimeSheet ts12 = new TimeSheet(unit, e, date, 8, 16, at, ActionType.MONTH_PLAN);
		timeSheetRepository.save(ts12);

		// Update 1 instance and create new instance
		ts11.setLastActionType(ActionType.MONTH_PLAN_ADJUST);
		timeSheetRepository.save(ts11);
		
		TimeSheet ts2 = new TimeSheet(unit, e, date, 8, 12, at, ActionType.MONTH_PLAN_ADJUST);
		timeSheetRepository.save(ts2);
		
		List<TimeSheet> monthTs1 = timeSheetRepository.getMonthTimeSheet(day, unit, ActionType.MONTH_PLAN);
		assertTrue( 2 == monthTs1.size());

		List<TimeSheet> dayTs1 = timeSheetRepository.getDayTimeSheet(day, unit, ActionType.MONTH_PLAN);
		assertTrue( 2 == dayTs1.size());

		List<TimeSheet> monthTs2 = timeSheetRepository.getMonthTimeSheet(day, unit, ActionType.MONTH_PLAN_ADJUST);
		assertTrue( 2 == monthTs2.size());

		List<TimeSheet> dayTs2 = timeSheetRepository.getDayTimeSheet(day, unit, ActionType.MONTH_PLAN_ADJUST);
		assertTrue( 2 == dayTs2.size());
		
		List<TimeSheet> monthTs3 = timeSheetRepository.getMonthTimeSheet(day, unit, ActionType.MONTH_PLAN_ADJUST);
		assertTrue( 2 == monthTs3.size());

		List<TimeSheet> dayTs3 = timeSheetRepository.getDayTimeSheet(day, unit, ActionType.MONTH_PLAN_ADJUST);
		assertTrue( 2 == dayTs3.size());
	}
	
	@Test
	@Transactional
	public void testDeleteTimeSheet() {
		String day = "2012-02-14";
		Date date = DateHelper.getDate(day);
		Unit unit = unitRepository.getRoot("XX");
		
		Employee e = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);
		employeeRepository.save(e);
		
		AttendanceType at = new AttendanceType("Day", 8, 16);
		attendanceTypeRepository.save(at);
		
		// Create 2 instance
		TimeSheet ts11 = new TimeSheet(unit, e, date, 8, 16, at, ActionType.MONTH_PLAN);
		timeSheetRepository.save(ts11);

		TimeSheet ts12 = new TimeSheet(unit, e, date, 8, 16, at, ActionType.MONTH_PLAN);
		timeSheetRepository.save(ts12);

		// Delete 1 instance and create new instance
		ts11.setLastActionType(ActionType.MONTH_PLAN_ADJUST);
		timeSheetRepository.save(ts11);
		
		TimeSheet ts2 = new TimeSheet(unit, e, date, 8, 12, at, ActionType.MONTH_PLAN_ADJUST);
		timeSheetRepository.disable(ts2);
		
		List<TimeSheet> monthTs1 = timeSheetRepository.getMonthTimeSheet(day, unit, ActionType.MONTH_PLAN);
		assertTrue( 2 == monthTs1.size());

		List<TimeSheet> dayTs1 = timeSheetRepository.getDayTimeSheet(day, unit, ActionType.MONTH_PLAN);
		assertTrue( 2 == dayTs1.size());

		List<TimeSheet> monthTs2 = timeSheetRepository.getMonthTimeSheet(day, unit, ActionType.MONTH_PLAN_ADJUST);
		assertTrue( 1 == monthTs2.size());

		List<TimeSheet> dayTs2 = timeSheetRepository.getDayTimeSheet(day, unit, ActionType.MONTH_PLAN_ADJUST);
		assertTrue( 1 == dayTs2.size());
		
		List<TimeSheet> monthTs3 = timeSheetRepository.getMonthTimeSheet(day, unit, ActionType.MONTH_PLAN_ADJUST);
		assertTrue( 1 == monthTs3.size());

		List<TimeSheet> dayTs3 = timeSheetRepository.getDayTimeSheet(day, unit, ActionType.MONTH_PLAN_ADJUST);
		assertTrue( 1 == dayTs3.size());
		
		
		int cnt = entityManager.createQuery("delete from TimeSheet ts where ts.unit.id = :unitId " +
								"and ts.date >= :beginDate and ts.date < :endDate and " +
								"ts.actionType <= :actionType")
					.setParameter("unitId", unit.getId())
					.setParameter("beginDate", DateHelper.getDate("2012-02-01"))
					.setParameter("endDate", DateHelper.getDate("2012-02-29"))
					.setParameter("actionType", TimeSheet.ActionType.MONTH_PLAN_ADJUST)
					.executeUpdate();
		assertTrue(3 == cnt);
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	@Transactional
	public void testQueryActualTimeSheet() {
		String day = "2012-02-14";
		Date date = DateHelper.getDate(day);
		Unit unit = unitRepository.getRoot("XX");
		
		Employee e = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);
		e.setJob(new Job(unit, null, Collections.<JobPosition>emptyList(), JobStatus.UNKNOWN, new Date()));
		e.getJob().setUnit(unit);
		e.setGender(Gender.MALE);
		employeeRepository.save(e);
		
		AttendanceType at1 = new AttendanceType("Day1", 8, 16);
		attendanceTypeRepository.save(at1);
		AttendanceType at2 = new AttendanceType("Day2", 8, 16);
		attendanceTypeRepository.save(at2);
		AttendanceType at3 = new AttendanceType("Day3", 8, 16);
		attendanceTypeRepository.save(at3);
		
		
		QueryActualTimeSheetCommand command = new QueryActualTimeSheetCommand();
		command.setUnitId(unit.getId());
		command.setBeginTime("2012-01-01");
		command.setEndTime("2012-02-29");
		command.setattendanceTypeIds(String.format("%d,%d,%d,", at1.getId(), at2.getId(), at3.getId()));
		
		System.out.println(command.toSQL(unit.getLeft(), unit.getRight()));
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(command.toSQL(unit.getLeft(), unit.getRight()));
		assertTrue(3 == rows.size());
		
		Employee ex = new Employee(new EmployeeId("0002"), "Alex Tang", Gender.MALE, date, date, date);
		ex.setGender(Gender.MALE);
		ex.setJob(new Job(unit, null, Collections.<JobPosition>emptyList(), JobStatus.UNKNOWN, new Date()));
		employeeRepository.save(ex);
		
		List<Map<String, Object>> rowsEx = jdbcTemplate.queryForList(command.toSQL(unit.getLeft(), unit.getRight()));
		assertTrue(6 == rowsEx.size());

		command.setEmployeeId(e.getId());
		List<Map<String, Object>> rowsEx2 = jdbcTemplate.queryForList(command.toSQL(unit.getLeft(), unit.getRight()));
		assertTrue(3 == rowsEx2.size());

		QueryTimeSheetByPropertyCommand cmd1 = new QueryTimeSheetByPropertyCommand();
		cmd1.setUnitId(unit.getId());
		cmd1.setBeginTime("2012-01-01");
		cmd1.setEndTime("2012-02-29");
		cmd1.setattendanceTypeIds(String.format("%d,%d,%d,", at1.getId(), at2.getId(), at3.getId()));
		
		System.out.println(cmd1.toSQL(unit.getLeft(), unit.getRight()));
		
		QueryVacationCommand xCmd  = new QueryVacationCommand();
		xCmd.setUnitId(unit.getId());
		xCmd.setDate("2012-01-01");
		System.out.println(xCmd.toSQL(unit.getLeft(), unit.getRight()));

	}
	
	
}
