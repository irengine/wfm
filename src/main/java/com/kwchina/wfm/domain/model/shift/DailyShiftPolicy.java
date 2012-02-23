package com.kwchina.wfm.domain.model.shift;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DailyShiftPolicy extends BaseShiftPolicy {

	private AttendanceType workAttendanceType;
	private AttendanceType holidayAttendanceType;
	
	private HolidaySpecification holidaySpecification;
	private WeekendSpecification weekendSpecification;
	
	public DailyShiftPolicy(AttendanceType workAttendanceType, AttendanceType holidayAttendanceType, List<String> holidays, String weekends, Map<String, String> daysChanged) {
		this.workAttendanceType = workAttendanceType;
		this.holidayAttendanceType = holidayAttendanceType;
		
		this.holidaySpecification = new HolidaySpecification(holidays);
		this.weekendSpecification = new WeekendSpecification(weekends, daysChanged);
	}
	
	@Override
	public AttendanceType getAttendanceType(Date date) {
		
		if (holidaySpecification.isSatisfiedBy(date) || weekendSpecification.isSatisfiedBy(date))
			return holidayAttendanceType;
		else
			return workAttendanceType;
	}
}
