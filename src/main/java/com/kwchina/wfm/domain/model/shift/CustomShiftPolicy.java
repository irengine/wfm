package com.kwchina.wfm.domain.model.shift;

import java.util.Date;
import java.util.List;

import com.kwchina.wfm.infrastructure.common.DateHelper;

public class CustomShiftPolicy extends BaseShiftPolicy {
	
	private Date startDate;
	private int offset;
	private List<AttendanceType> attendanceTypes;
	
	public CustomShiftPolicy(Date startDate, int offset, List<AttendanceType> attendanceTypes) {
		this.startDate = startDate;
		this.offset = offset;
		this.attendanceTypes = attendanceTypes;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<AttendanceType> getAttendanceTypes() {
		return attendanceTypes;
	}

	public void setAttendanceTypes(List<AttendanceType> attendanceTypes) {
		this.attendanceTypes = attendanceTypes;
	}
	
	@Override
	public AttendanceType getAttendanceType(Date date) {
		int days = DateHelper.daysBetween(startDate, date);
		int interval = attendanceTypes.size();
		int index = (days - offset) % interval;
		if (index < 0)
			index = interval + index;
		return attendanceTypes.get(index);
	}
}
