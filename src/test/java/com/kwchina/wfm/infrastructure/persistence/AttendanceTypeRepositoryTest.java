package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class AttendanceTypeRepositoryTest {
	
	@Autowired
	private AttendanceTypeRepository attendanceTypeRepository;

	@Test
	@Transactional
	public void testSaveAttendanceType() {
		AttendanceType at = new AttendanceType("Day", 8, 16);
		attendanceTypeRepository.save(at);
		
		Long count = attendanceTypeRepository.getRowsCount("", true);
		assertEquals(new Long(1), count);
	}
	
	@Test
	@Transactional
	public void testSaveAttendanceTypeWithPreferences() {
		AttendanceType at = new AttendanceType("Day", 8, 16);
		Set<Preference> preferences = new HashSet<Preference>();
		
		preferences.add(new Preference("1+1=", "2"));
		preferences.add(new Preference("1*1=", "1"));
		
		at.setPreferences(preferences);
		
		attendanceTypeRepository.save(at);
		
		Long count = attendanceTypeRepository.getRowsCount("", true);
		assertEquals(new Long(1), count);

		AttendanceType at2 = new AttendanceType("Day2", 8, 16);
		Set<Preference> preferences2 = new HashSet<Preference>();
		
		preferences2.add(new Preference("2+2=", "4"));
		preferences2.add(new Preference("2*2=", "4"));
		
		at2.setPreferences(preferences2);
		
		attendanceTypeRepository.save(at2);
		
		count = attendanceTypeRepository.getRowsCount("", true);
		assertEquals(new Long(2), count);
	
	}
}
