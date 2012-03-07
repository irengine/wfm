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
	
	public String toSQL(Long leftId, Long rightId) {
		List<String> firstConditions = new ArrayList<String>();
		List<String> secondConditions = new ArrayList<String>();
		
		if (!(null == this.unitId || this.unitId.equals(0))) {
			firstConditions.add(String.format("u.leftId >= %d and u.rightId <= %d", leftId, rightId));
			secondConditions.add(String.format("u.leftId >= %d and u.rightId <= %d", leftId, rightId));
		}
		
		if (!(null == this.employeeId || this.employeeId.equals(0))) {
			firstConditions.add(String.format("e.id = %d", this.employeeId));
			secondConditions.add(String.format("ts.employeeId = %d", this.employeeId));
		}
		
		if (!StringUtils.isEmpty(this.beginTime)) {
			secondConditions.add(String.format("ts.date >= '%s'", beginTime));
		}

		if (!StringUtils.isEmpty(this.endTime)) {
			secondConditions.add(String.format("ts.date <= '%s'", endTime));
		}
		
		String secondCondition = " where ts.enable = true and ts.lastActionType is null and " + StringUtils.join(secondConditions, " AND ");
		
		if (!StringUtils.isEmpty(this.attendanceTypeIds)) {
			firstConditions.add(String.format("ats.id in (%s0)", this.attendanceTypeIds));
		}
		
		String firstCondition = (0 == firstConditions.size()) ? "" : " where " + StringUtils.join(firstConditions, " AND ");
		
		
		String syntax = "select x.*, count(ts.attendanceTypeId) as days " +
				"from (select e.Id as employeeId, e.employeeId as employeeCode, e.name as employeeName, " +
					"ats.Id as attendanceTypeId, ats.name as attendanceTypeName " +
					"from t_attendance_types ats, t_employees e inner join t_units u on e.unitId = u.id" +
					"%s) x " +
				"left join (select ts.*  from t_timesheet ts inner join t_units u on ts.unitId = u.id %s) ts on x.employeeId = ts.employeeId and x.attendanceTypeId = ts.attendanceTypeId " +
				"group by employeeId, employeeCode, employeeName, attendanceTypeId, attendanceTypeName";
		
		return String.format(syntax, firstCondition, secondCondition);
	}
}
