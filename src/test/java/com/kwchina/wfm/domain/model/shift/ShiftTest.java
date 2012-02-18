package com.kwchina.wfm.domain.model.shift;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.CustomShiftPolicy;
import com.kwchina.wfm.infrastructure.common.DateHelper;

public class ShiftTest {

	private AttendanceType fullDayAttendance = new AttendanceType("全", 0, 24);
	private AttendanceType morningAttendance = new AttendanceType("日", 8, 16);
	private AttendanceType afternoonAttendance = new AttendanceType("中", 16, 24);
	private AttendanceType nightAttendance = new AttendanceType("夜", 24, 32);
	private AttendanceType breakDayAttendance = new AttendanceType("休", 0, 24);
	
	@Test
	public void testDailyShiftSpecification() {
		
	}

	@Test
	public void testCustomShiftSpecification() {
		Date startDate = DateHelper.getDate("2012-01-01");
		List<AttendanceType> ps = new ArrayList<AttendanceType>();
		ps.add(fullDayAttendance);
		ps.add(breakDayAttendance);
		ps.add(breakDayAttendance);
		ps.add(breakDayAttendance);
		
		CustomShiftPolicy p0 = new CustomShiftPolicy(startDate, 0, ps);
		CustomShiftPolicy p1 = new CustomShiftPolicy(startDate, 1, ps);
		CustomShiftPolicy p2 = new CustomShiftPolicy(startDate, 2, ps);
		CustomShiftPolicy p3 = new CustomShiftPolicy(startDate, 3, ps);
		
		assertEquals(fullDayAttendance, p0.getAttendanceType(DateHelper.getDate("2012-01-01")));
		assertEquals(fullDayAttendance, p1.getAttendanceType(DateHelper.getDate("2012-01-02")));
		assertEquals(fullDayAttendance, p2.getAttendanceType(DateHelper.getDate("2012-01-03")));
		assertEquals(fullDayAttendance, p3.getAttendanceType(DateHelper.getDate("2012-01-04")));

		printHeader();
		printResult(p0);
		printResult(p1);
		printResult(p2);
		printResult(p3);
		
		List<AttendanceType> ts = new ArrayList<AttendanceType>();
		ts.add(morningAttendance);
		ts.add(morningAttendance);
		ts.add(afternoonAttendance);
		ts.add(afternoonAttendance);
		ts.add(breakDayAttendance);
		ts.add(nightAttendance);
		ts.add(nightAttendance);
		ts.add(breakDayAttendance);
		
		CustomShiftPolicy t0 = new CustomShiftPolicy(startDate, 0, ts);
		CustomShiftPolicy t1 = new CustomShiftPolicy(startDate, 2, ts);
		CustomShiftPolicy t2 = new CustomShiftPolicy(startDate, 4, ts);
		CustomShiftPolicy t3 = new CustomShiftPolicy(startDate, 6, ts);
		
		assertEquals(morningAttendance, t0.getAttendanceType(DateHelper.getDate("2012-01-01")));
		assertEquals(nightAttendance, t1.getAttendanceType(DateHelper.getDate("2012-01-01")));
		assertEquals(breakDayAttendance, t2.getAttendanceType(DateHelper.getDate("2012-01-01")));
		assertEquals(afternoonAttendance, t3.getAttendanceType(DateHelper.getDate("2012-01-01")));

		assertEquals(morningAttendance, t0.getAttendanceType(DateHelper.getDate("2012-01-02")));
		assertEquals(breakDayAttendance, t1.getAttendanceType(DateHelper.getDate("2012-01-02")));
		assertEquals(nightAttendance, t2.getAttendanceType(DateHelper.getDate("2012-01-02")));
		assertEquals(afternoonAttendance, t3.getAttendanceType(DateHelper.getDate("2012-01-02")));
		
		printHeader();
		printResult(t0);
		printResult(t1);
		printResult(t2);
		printResult(t3);
	}

	@SuppressWarnings("deprecation")
	private void printHeader() {
		List<Date> days = DateHelper.getDaysOfMonth(2012, Calendar.JANUARY);
		for(Date day : days) {
			System.out.print(day.getDate());
			System.out.print("\t");
		}
		System.out.println("");
	}
	
	private void printResult(CustomShiftPolicy p) {
		List<Date> days = DateHelper.getDaysOfMonth(2012, Calendar.JANUARY);

		for(Date day : days) {
			System.out.print(p.getAttendanceType(day).getName());
			System.out.print("\t");
		}
		System.out.println("");
		
	}
}
