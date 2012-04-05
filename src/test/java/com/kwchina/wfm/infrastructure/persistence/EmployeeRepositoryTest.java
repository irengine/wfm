package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.Employee.Gender;
import com.kwchina.wfm.domain.model.employee.EmployeeId;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.employee.Job;
import com.kwchina.wfm.domain.model.employee.JobPosition;
import com.kwchina.wfm.domain.model.employee.JobTitle;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.infrastructure.common.DateHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class EmployeeRepositoryTest {
	// set @Rollback(false) to see if data exists

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Test
	public void testOnlyOneEmployeeInSetWhichHasSameEmployeeId() {
		Employee e1 = new Employee();
		e1.setEmployeeId(new EmployeeId("00001"));
		e1.setName("Alex");
		Employee e2 = new Employee();
		e2.setEmployeeId(new EmployeeId("00001"));
		e2.setName("Irene");
		Employee e3 = new Employee();
		e3.setEmployeeId(new EmployeeId("00002"));
		e3.setName("Irene");
		
		Set<Employee> es = new LinkedHashSet<Employee>();
		es.add(e1);
		assertTrue(1 == es.size());
		es.add(e2);
		assertTrue(1 == es.size());
		es.add(e3);
		assertTrue(2 == es.size());
	}

	@Test
	@Transactional
	public void testSaveEmployeeWithJob() throws ParseException {
		Date date = DateHelper.getDate("2012-02-14");
		Employee employee = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);
		
		employee.setJob(Job.UNKNOWN);

		assertNull(employee.getId());
		
		employeeRepository.save(employee);
		
		assertNotNull(employee.getId());
	}
	
	@Test
	@Transactional
	public void testSaveEmployeeWithSameEmployeeId() throws ParseException {
		Date date = DateHelper.getDate("2012-02-14");
		Employee employee = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);

		assertNull(employee.getId());
		
		employeeRepository.save(employee);
		
		assertNotNull(employee.getId());
		
		try {
			Employee e =  new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);
			employeeRepository.save(e);
			fail("Employee id should not be same.");
		}
		catch(Exception expected) {
			
		}
	}
	
	@Test
	@Transactional
	public void testCantChangeEmployeeId() throws ParseException {
		Date date = DateHelper.getDate("2012-02-14");
		Employee employee = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);

		assertNull(employee.getId());
		
		employeeRepository.save(employee);
		
		assertNotNull(employee.getId());
		
		employee.setEmployeeId(new EmployeeId("0002"));
		employeeRepository.save(employee);
	}

	
	@Test
	@Transactional
	public void testDisableEmployee() throws ParseException {
		Date date = DateHelper.getDate("2012-02-14");
		Employee employee = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);

		employeeRepository.save(employee);

		Long rowsBefore1 = employeeRepository.getRowsCount("");
		Long rowsBefore2 = employeeRepository.getRowsCount("", true);
		employeeRepository.disable(employee);
		Long rowsAfter1 = employeeRepository.getRowsCount("");
		Long rowsAfter2 = employeeRepository.getRowsCount("", true);
		
		assertEquals(rowsBefore1, (Long)(rowsAfter1 + 1));
		assertEquals(rowsBefore2, rowsAfter2);
	}
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	@Transactional
	public void testHireEmployee() throws ParseException {
		Date date = DateHelper.getDate("2012-02-14");
		Employee employee = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);

		Unit unit = new Unit("X");
		JobTitle title = new JobTitle("M", "Manager", 1);
		List<JobPosition> positions = new ArrayList<JobPosition>();
		positions.add(new JobPosition("DM"));
		
		entityManager.persist(unit);
		entityManager.persist(title);
		
		employee.hire(unit, title, positions, new Date());

		assertNull(employee.getId());
		
		employeeRepository.save(employee);
		
		assertNotNull(employee.getId());
		assertNotNull(employee.getJob());
	}
	
	@Test
	public void testGetEmployees() {
		Long cnt = (Long)entityManager.createQuery("SELECT COUNT(*) FROM Employee WHERE enable=true AND job.unit.id = 1").getSingleResult();
		assertEquals(new Long(0), cnt);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetEmployeesByPreference() {
		
		List<Employee> es= entityManager.createQuery("FROM Employee e, IN(e.preferences) ps WHERE enable=true AND id=1 AND e.job.unit.id = 1 AND ps.key = :key")
				.setParameter("key", "xxx")
				.getResultList();
		assertTrue(0 == es.size());
	}
	
	@Test
	public void testFindAllEmployees() {
		employeeRepository.findAllByUnitId(new Long(1));
	}
	
	@Test
	public void testFindByCode() {
		employeeRepository.findByCode("1");
	}
	
	@Test
	@Transactional
	public void testCreateVacation() {
		employeeRepository.calculateVacation(DateHelper.getDate("2012-01-01"), new Long(1));
	}


}
