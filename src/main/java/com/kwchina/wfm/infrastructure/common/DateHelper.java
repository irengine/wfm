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
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);
		beginCalendar.add(Calendar.MONTH, -1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);

		List<Date> days = new ArrayList<Date>();
		
		while(beginCalendar.before(endCalendar)) {
			days.add(beginCalendar.getTime());
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return days;
	}
	
	public static List<Date> getDaysOfMonth(int year, int month, Date beginDate, Date endDate) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);
		beginCalendar.add(Calendar.MONTH, -1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);
		
		Calendar beginScope = Calendar.getInstance();
		beginScope.setTime(beginDate);

		Calendar endScope = Calendar.getInstance();
		endScope.setTime(endDate);
		
		beginCalendar = beginCalendar.before(beginScope) ? beginScope : beginCalendar;
		endCalendar = endCalendar.after(endScope) ? endScope : endCalendar;

		List<Date> days = new ArrayList<Date>();
		
		while(beginCalendar.before(endCalendar)) {
			days.add(beginCalendar.getTime());
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return days;
	}
	
	public static Date addDay(Date day, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.add(Calendar.DATE, amount);
		
		return c.getTime();
	}
	
	// TODO: move begin date and end date into system properties
	private static int DAY_OF_MONTH = 26;
	
	public static List<Date> getDaysOfMonth(String d) {
		Date date = getDate(d);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return getDaysOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}
	
	public static Date getMonth() {
		Calendar calendar = Calendar.getInstance();
		
		if (calendar.get(Calendar.DAY_OF_MONTH) >= DAY_OF_MONTH)
			calendar.add(Calendar.MONTH, 1);

		calendar.set(Calendar.DAY_OF_MONTH, 1);

		return calendar.getTime();
	}
	
	public static Date getBeginDateOfMonth(String month) {
		// Get first and last day for month
		Date date = DateHelper.getDate(month);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH);
		Date beginDate = calendar.getTime();
		
		return beginDate;
	}
	
	public static Date getEndDateOfMonth(String month) {
		// Get first and last day for month
		Date date = DateHelper.getDate(month);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH);
		Date endDate = calendar.getTime();
		
		return endDate;
	}

	public static long getNextRuntimeMinutes(int hours) {
		Calendar now = Calendar.getInstance();
		
		Calendar next = Calendar.getInstance();
		
		if (now.get(Calendar.HOUR_OF_DAY) > hours)
			next.add(Calendar.DAY_OF_MONTH, 1);
		next.set(Calendar.HOUR_OF_DAY, hours);
		next.set(Calendar.MINUTE, 0);
		next.set(Calendar.SECOND, 0);
		
		long diffMinutes = (next.getTimeInMillis() - now.getTimeInMillis()) / (60 * 1000); 
		
		return diffMinutes;
	}
	
	public static long MINUTES_ONE_DAY = 24 * 60;
}
