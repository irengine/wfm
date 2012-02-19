package com.kwchina.wfm.domain.model.shift;

import java.util.Date;

public class DailyShiftPolicy extends BaseShiftPolicy {

	private AttendanceType workAttendanceType;
	private AttendanceType holidayAttendanceType;
	
	private HolidaySpecification holidaySpecification;
	
	public DailyShiftPolicy(AttendanceType workAttendanceType, AttendanceType holidayAttendanceType) {
		this.workAttendanceType = workAttendanceType;
		this.holidayAttendanceType = holidayAttendanceType;
	}
	
	@Override
	public AttendanceType getAttendanceType(Date date) {
		
		if (holidaySpecification.isSatisfiedBy(date))
			return holidayAttendanceType;
		else
			return workAttendanceType;
	}
}
