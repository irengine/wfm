package com.kwchina.wfm.domain.model.employee;

import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Period;
import org.junit.Test;

import com.kwchina.wfm.infrastructure.common.DateHelper;

public class VacationTest {

	@Test
	public void testCalculateMonthDifference() {
		DateTime start = DateHelper.getDateTime("2009-01-14");
		DateTime end = DateHelper.getDateTime("2011-01-12");
		Period p = new Period(start, end);
		assertTrue( 1 == p.getYears());
		assertTrue( 11 == p.getMonths());
		assertTrue( 1 == end.get(DateTimeFieldType.monthOfYear()));
		
		int i = 0;
		i = 13 /5;
		assertTrue(2 == i);
		
		assertTrue( 5 == getDays(p.getYears(), end.get(DateTimeFieldType.monthOfYear())));
	}
	
	private int getDays(int years, int month) {
		int days = 0;
		
		if (years == 0)
			days = 0;
		else if (years == 1)
			days = (5 * (12 - month + 1)) / 12;
		else if (years > 1 && years < 10)
			days = 5;
		else if (years == 10)
			days = 5 * (month - 1) / 12 + 10 * (12 - month + 1) /12 ;
		else if (years > 10 && years < 20)
			days = 10;
		else if (years == 20)
			days = 10 * (month - 1) / 12 + 15 * (12 - month + 1) /12 ;
		else if (years > 20)
			days = 15;
		
		return days;
	}

}
