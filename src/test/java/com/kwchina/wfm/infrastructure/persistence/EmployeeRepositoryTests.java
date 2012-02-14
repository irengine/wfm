package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Employee;
import com.kwchina.wfm.domain.model.organization.EmployeeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class EmployeeRepositoryTests {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Test
	@Transactional
	public void testSaveEmployee() throws ParseException {
		Date date = DateUtils.parseDate("2012-02-14",new String[]{"yyyy-MM-dd"});
		Employee employee = new Employee("0001", "Alex Tang", date, date, date);

		assertNull(employee.getId());

		employeeRepository.save(employee);
		
		assertNotNull(employee.getId());
	}
	
	@Test
	@Transactional
	public void testDisableEmployee() throws ParseException {
		Date date = DateUtils.parseDate("2012-02-14",new String[]{"yyyy-MM-dd"});
		Employee employee = new Employee("0001", "Alex Tang", date, date, date);

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
