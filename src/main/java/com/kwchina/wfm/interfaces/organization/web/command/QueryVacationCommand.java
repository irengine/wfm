package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kwchina.wfm.domain.model.employee.Vacation;
import com.kwchina.wfm.infrastructure.common.DateHelper;

public class QueryVacationCommand {
	
	private Vacation.Type type;
	private String beginTime;
	private String endTime;
	
	private Long unitId;
	private Long employeeId;
	private String date;
	private String unitIds;
	private String employeeName;
	
	public Vacation.Type getType() {
		return type;
	}
	public void setType(Vacation.Type type) {
		this.type = type;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
		this.beginTime = date;
		this.endTime = DateHelper.getString(DateHelper.addYear(DateHelper.getDate(date), 1));
	}
	public String getUnitIds() {
		return unitIds;
	}
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String toSQL(Long leftId, Long rightId) {
		List<String> firstConditions = new ArrayList<String>();
		List<String> secondConditions = new ArrayList<String>();
		
		secondConditions.add(String.format("v.type = '%s'", this.getType().name()));
		
		if (!(null == this.unitId || this.unitId.equals(0))) {
			firstConditions.add(String.format("u.leftId >= %d and u.rightId <= %d", leftId, rightId));
		}
		
		if (!(null == this.getEmployeeId() || this.getEmployeeId().equals(0))) {
			firstConditions.add(String.format("e.id = %d", this.getEmployeeId()));
			secondConditions.add(String.format("v.employeeId = %d", this.getEmployeeId()));
		}

		if (!(null == this.employeeName || this.employeeName.isEmpty())) {
			firstConditions.add(String.format("e.name = '%s'", this.employeeName));
			//secondConditions.add(String.format("x.employeeName = '%s'", this.employeeName));
		}
		
		if (!StringUtils.isEmpty(this.beginTime)) {
			//secondConditions.add(String.format("v.month >= '%s' and v.month < date_add('%s', INTERVAL 1 YEAR)", date, date));
			secondConditions.add(String.format("v.month >= '%s' and v.month <= '%s'", beginTime, endTime));
		}
		
		String firstCondition = (0 == firstConditions.size()) ? " where e.enable = 1 " : " where e.enable = 1 and " + StringUtils.join(firstConditions, " AND ");
		String secondCondition = (0 == secondConditions.size()) ? "" : " where " + StringUtils.join(secondConditions, " AND ");
		
		
		String syntax = "select x.*, v.month, v.lastBalance, v.amount, v.lastBalance + v.balance - v.amount as balance " +
				"from (select e.Id as employeeId, e.employeeId as employeeCode, e.name as employeeName, e.beginDateOfJob, u.id as unitId, u.name as unitName, u.uriName " +
					"from t_employees e inner join t_units u on e.unitId = u.id" +
					"%s) x " +
				"left join (select v.*  from t_employee_vacations v %s) v on x.employeeId = v.employeeId";
		
		return String.format(syntax, firstCondition, secondCondition);
	}
}
