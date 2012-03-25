package com.kwchina.wfm.interfaces.report;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.infrastructure.common.DateHelper;

public class MonthTimeSheetReport {
	
	private Map<String, Map<String, Set<TimeSheet>>> data = new HashMap<String, Map<String, Set<TimeSheet>>>();
	private List<Date> days;

	public MonthTimeSheetReport() {
		
	}
	
	public Map<String, Map<String, Set<TimeSheet>>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, Set<TimeSheet>>> data) {
		this.data = data;
	}

	public List<Date> getDays() {
		return days;
	}

	public void setDays(List<Date> days) {
		this.days = days;
	}

	public void fill(Set<TimeSheet> list, List<Date> days) {
		this.setDays(days);
		
		for (TimeSheet ts : list) {
			String key = ts.getEmployee().getEmployeeId().toString();
			
			if (!getData().containsKey(key)) {
				getData().put(key, getRows(days));
			}
			getData().get(key).get(DateHelper.getString(ts.getDate())).add(ts);
		}
	}
	
	private Map<String, Set<TimeSheet>> getRows(List<Date> days) {
		Map<String, Set<TimeSheet>> rows = new TreeMap<String, Set<TimeSheet>>();
		for (Date day : days) {
			rows.put(DateHelper.getString(day), new LinkedHashSet<TimeSheet>());
		}
		
		return rows;
	}
}
