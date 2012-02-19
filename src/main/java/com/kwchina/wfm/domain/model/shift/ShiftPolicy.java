package com.kwchina.wfm.domain.model.shift;

import java.util.Date;

public interface ShiftPolicy {

	AttendanceType getAttendanceType(Date date);
	
}
