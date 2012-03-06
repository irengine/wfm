package com.kwchina.wfm.infrastructure.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class DateHelper {

	public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	public static Date getDate(String d) {
		try {
			return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(d);
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static DateTime getDateTime(String dt) {
		Date d = getDate(dt);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		return new DateTime().withDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String getString(Date d) {
		return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(d);
	}
	
	public static int daysBetween(Date past, Date today) {
		return Days.daysBetween(new DateTime(past), new DateTime(today)).getDays();
	}
	
	public static List<Date> getDaysOfMonth(int year, int month) {
		int firstDay = 1;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, firstDay);
		
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		List<Date> days = new ArrayList<Date>();
		for (int i = firstDay; i <= lastDay; i++) {
			Calendar c = Calendar.getInstance();
			c.set(year, month, i, 0, 0, 0);
			days.add(c.getTime());
		}
		
		return days;
	}
	
	public static List<Date> getDaysOfMonth(String d) {
		Date date = getDate(d);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return getDaysOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}

}
