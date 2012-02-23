package com.kwchina.wfm.domain.model.shift;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.kwchina.wfm.infrastructure.common.DateHelper;

public class HolidaySpecificationTest {

	@Test
	public void testGetDayList() {
		int year = 2012;
		int month = Calendar.JANUARY;	// month is base 0
		int firstDay = 1;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, firstDay);
		
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		assertTrue(31 == lastDay);
		
		List<Date> days = new ArrayList<Date>();
		for (int i = firstDay; i <= lastDay; i++) {
			Calendar c = Calendar.getInstance();
			c.set(year, month, i);
			Date d = c.getTime();
			days.add(d);
		}
		
		assertEquals(31, days.size());
		
		initialHolidayDefinition();

		HolidaySpecification hs = new HolidaySpecification(holidays);
		WeekendSpecification ws = new WeekendSpecification(weekends, daysChanged);
		for(Date d : days) {
			System.out.print(DateHelper.getString(d));
			if (hs.isSatisfiedBy(d))
				System.out.println(" --- holiday");
			else if (ws.isSatisfiedBy(d))
				System.out.println(" --- weekend");
			else
				System.out.println("");
		}
	}
	
	private String weekends = "1,7";
	private List<String> holidays = new ArrayList<String>();
	private Map<String, String> daysChanged = new HashMap<String, String>();
	
	private void initialHolidayDefinition() {
		this.holidays.add("2012-01-01");
		this.holidays.add("2012-01-22");
		this.holidays.add("2012-01-23");
		this.holidays.add("2012-01-24");
		
		this.daysChanged.put("2011-12-31", "2012-01-02");
		this.daysChanged.put("2012-01-01", "2012-01-03");
		this.daysChanged.put("2012-01-21", "2012-01-25");
		this.daysChanged.put("2012-01-22", "2012-01-26");
		this.daysChanged.put("2012-01-27", "2012-01-29");
		
		this.daysChanged.put("2012-01-02", "2011-12-31");
		this.daysChanged.put("2012-01-03", "2012-01-01");
		this.daysChanged.put("2012-01-25", "2012-01-21");
		this.daysChanged.put("2012-01-26", "2012-01-22");
		this.daysChanged.put("2012-01-29", "2012-01-27");
	}
	
	@Test
	public void getLastDay() {
		int year = 2012;

		String s = String.format("%d-01-01", year);
		Date d = DateHelper.getDate(s);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);

		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		
		assertTrue(366 == lastDay);
		
		for(int i = 0; i< lastDay; i++) {
			Date x = calendar.getTime();
			System.out.println(DateHelper.getString(x));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
	}

}
