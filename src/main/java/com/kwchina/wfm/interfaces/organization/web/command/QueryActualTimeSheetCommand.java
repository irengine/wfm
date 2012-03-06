package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class QueryActualTimeSheetCommand {

	/*
	 * unitId=3&employeeId=2&beginTime=2012-02-01&endTime=2012-02-20&attendanceTypes=日班,中班,公休,
	 */
	
	private Long unitId;
	private Long employeeId;
	private String beginTime;
	private String endTime;
	private String attendanceTypeIds;
	
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getattendanceTypeIds() {
		return attendanceTypeIds;
	}
	public void setattendanceTypeIds(String attendanceTypeIds) {
		this.attendanceTypeIds = attendanceTypeIds;
	}
	
	public String toSQL() {
		String syntax = "select ts.employeeId, e.name as employeeName, ts.attendanceTypeId, ats.name as attendanceTypeName, count(ts.attendanceTypeId) as days " +
				"from t_timesheet ts inner join t_employees e on ts.employeeId = e.id " +
				"inner join t_attendance_types ats on ts.attendanceTypeId = ats.id ";
		List<String> conditions = new ArrayList<String>();
		
		if (!(null == this.unitId || this.unitId.equals(0))) {
			conditions.add(String.format("ts.unitId = %d", this.unitId));
		}
		
		if (!(null == this.employeeId || this.employeeId.equals(0))) {
			conditions.add(String.format("ts.employeeId = %d", this.employeeId));
		}
		
		if (!StringUtils.isEmpty(this.beginTime)) {
			conditions.add(String.format("ts.date >= '%s'", beginTime));
		}

		if (!StringUtils.isEmpty(this.endTime)) {
			conditions.add(String.format("ts.date <= '%s'", endTime));
		}

		if (!StringUtils.isEmpty(this.attendanceTypeIds)) {
			conditions.add(String.format("ts.attendanceTypeId in (%s0)", this.attendanceTypeIds));
		}
		
		if (conditions.size() > 0)
			syntax = syntax + " where ts.enable = true and ts.lastActionType is null and " + StringUtils.join(conditions, " AND ")  + " group by ts.employeeId, e.name, ts.attendanceTypeId, ats.name";
		return syntax;
	}
}
