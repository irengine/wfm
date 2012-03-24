package com.kwchina.wfm.infrastructure.common;

import java.util.Calendar;

import org.junit.Test;

public class DateHelperTest {

	@Test
	public void testGetMinutes() {
		Calendar next = Calendar.getInstance();
		next.add(Calendar.DAY_OF_MONTH, 1);
		next.set(Calendar.HOUR_OF_DAY, 1);
		next.set(Calendar.MINUTE, 0);
		next.set(Calendar.SECOND, 0);

		Calendar now = Calendar.getInstance();
		
		long diffMinutes = (next.getTimeInMillis() - now.getTimeInMillis()) / (60 * 1000); 
		
		System.out.println(diffMinutes);
	}

	@Test
	public void testGetDifference() {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.set(2007, 01, 10);
		calendar2.set(2007, 07, 01);
		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);
		System.out.println("\nThe Date Different Example");
		System.out.println("Time in milliseconds: " + diff + " milliseconds.");
		System.out.println("Time in seconds: " + diffSeconds + " seconds.");
		System.out.println("Time in minutes: " + diffMinutes + " minutes.");
		System.out.println("Time in hours: " + diffHours + " hours.");
		System.out.println("Time in days: " + diffDays + " days.");

	}

}
