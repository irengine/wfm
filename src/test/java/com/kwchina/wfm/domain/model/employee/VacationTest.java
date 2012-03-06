package com.kwchina.wfm.domain.model.employee;

import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
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
	}

}
