package com.kwchina.wfm.interfaces.organization.web.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class QueryVacationCommand {
	
	private Long unitId;
	private Long employeeId;
	private String date;
	
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
	}
	
	public String toSQL(Long leftId, Long rightId) {
		List<String> firstConditions = new ArrayList<String>();
		List<String> secondConditions = new ArrayList<String>();
		
		if (!(null == this.unitId || this.unitId.equals(0))) {
			firstConditions.add(String.format("u.leftId >= %d and u.rightId <= %d", leftId, rightId));
		}
		
		if (!(null == this.getEmployeeId() || this.getEmployeeId().equals(0))) {
			firstConditions.add(String.format("e.id = %d", this.getEmployeeId()));
			secondConditions.add(String.format("v.employeeId = %d", this.getEmployeeId()));
		}
		
		if (!StringUtils.isEmpty(this.date)) {
			secondConditions.add(String.format("v.month >= '%s' and v.month < date_add('%s', INTERVAL 1 YEAR)", date, date));
		}
		
		String firstCondition = (0 == firstConditions.size()) ? " where e.enable = 1 " : " where e.enable = 1 and " + StringUtils.join(firstConditions, " AND ");
		String secondCondition = (0 == secondConditions.size()) ? "" : " where " + StringUtils.join(secondConditions, " AND ");
		
		
		String syntax = "select x.*, v.month, v.lastBalance, v.amount, v.lastBalance + v.balance - v.amount as balance " +
				"from (select e.Id as employeeId, e.employeeId as employeeCode, e.name as employeeName " +
					"from t_employees e inner join t_units u on e.unitId = u.id" +
					"%s) x " +
				"left join (select v.*  from t_employee_vacations v %s) v on x.employeeId = v.employeeId";
		
		return String.format(syntax, firstCondition, secondCondition);
	}
}
