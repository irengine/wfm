package com.kwchina.wfm.interfaces.report;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.TimeSheet;

public class MonthTimeSheetReport {
	
	Map<Employee, Map<Date, Set<TimeSheet>>> report = new HashMap<Employee, Map<Date, Set<TimeSheet>>>();

	public MonthTimeSheetReport() {
		
	}
	
	public void fill(Set<TimeSheet> list, List<Date> days) {
		for (TimeSheet ts : list) {
			if (!report.containsKey(ts.getEmployee())) {
				report.put(ts.getEmployee(), getRows(days));
			}
			report.get(ts.getEmployee()).get(ts.getDate()).add(ts);
		}
	}
	
	private Map<Date, Set<TimeSheet>> getRows(List<Date> days) {
		Map<Date, Set<TimeSheet>> rows = new HashMap<Date, Set<TimeSheet>>();
		for (Date day : days) {
			rows.put(day, new LinkedHashSet<TimeSheet>());
		}
		
		return rows;
	}
}
