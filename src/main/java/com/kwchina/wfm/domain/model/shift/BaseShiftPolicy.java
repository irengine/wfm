package com.kwchina.wfm.domain.model.shift;

import java.util.Date;

public class BaseShiftPolicy implements ShiftPolicy {

	@Override
	public AttendanceType getAttendanceType(Date date) {
		return null;
	}

}
