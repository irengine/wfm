package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.employee.DateHelper;
import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeId;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class EmployeeRepositoryTest {
	// set @Rollback(false) to see if data exists

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private UnitRepository unitRepository;
	
	@Test
	@Transactional
	public void testSaveEmployeeWithSameEmployeeId() throws ParseException {
		Unit unit = new Unit("X");
		unitRepository.save(unit);
		
		Date date = DateHelper.getDate("2012-02-14");
		Employee employee = new Employee(new EmployeeId("0001"), "Alex Tang", date, date, date);

		assertNull(employee.getId());
		
		employee.setUnit(unit);
		employeeRepository.save(employee);
		
		assertNotNull(employee.getId());
		assertNotNull(employee.getUnit());
		
		try {
			Employee e =  new Employee(new EmployeeId("0001"), "Alex Tang", date, date, date);
			employeeRepository.save(e);
			fail("Employee id should not be same.");
		}
		catch(Exception expected) {
			
		}
	}
	
	@Test
	@Transactional
	public void testCantChangeEmployeeId() throws ParseException {
		Unit unit = new Unit("X");
		unitRepository.save(unit);
		
		Date date = DateHelper.getDate("2012-02-14");
		Employee employee = new Employee(new EmployeeId("0001"), "Alex Tang", date, date, date);

		assertNull(employee.getId());
		
		employee.setUnit(unit);
		employeeRepository.save(employee);
		
		assertNotNull(employee.getId());
		assertNotNull(employee.getUnit());
		
		employee.setEmployeeId(new EmployeeId("0002"));
		employeeRepository.save(employee);
	}

	
	@Test
	@Transactional
	public void testDisableEmployee() throws ParseException {
		Date date = DateHelper.getDate("2012-02-14");
		Employee employee = new Employee(new EmployeeId("0001"), "Alex Tang", date, date, date);

		employeeRepository.save(employee);

		Long rowsBefore1 = employeeRepository.getRowsCount("");
		Long rowsBefore2 = employeeRepository.getRowsCount("", true);
		employeeRepository.disable(employee);
		Long rowsAfter1 = employeeRepository.getRowsCount("");
		Long rowsAfter2 = employeeRepository.getRowsCount("", true);
		
		assertEquals(rowsBefore1, (Long)(rowsAfter1 + 1));
		assertEquals(rowsBefore2, rowsAfter2);
	}

}
