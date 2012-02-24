package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class SystemPreferenceRepositoryTest {
	// set @Rollback(false) to see if data exists

	@Autowired
	private SystemPreferenceRepository systemPreferenceRepository;

	@Test
	@Transactional
	public void testManipulateHoliday() {
		String holiday = "2012-01-01";
		List<String> holidays;
		
		systemPreferenceRepository.addHoliday(holiday);
		holidays = systemPreferenceRepository.getHolidays();
		assertEquals(1, holidays.size());
		
		systemPreferenceRepository.removeHoliday(holiday);
		holidays = systemPreferenceRepository.getHolidays();
		assertEquals(0, holidays.size());
	}
	
	@Test
	@Transactional
	public void testManipulateDayChanged() {
		String before = "2012-01-01";
		String after = "2012-01-02";
		Map<String, String> daysChanged;
		
		systemPreferenceRepository.addDaysChanged(before, after);
		daysChanged = systemPreferenceRepository.getDaysChanged();
		assertEquals(2, daysChanged.size());
		
		systemPreferenceRepository.removeDaysChanged(before);
		daysChanged = systemPreferenceRepository.getDaysChanged();
		assertEquals(0, daysChanged.size());
	}

}
