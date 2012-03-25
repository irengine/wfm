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
import com.kwchina.wfm.interfaces.common.ReportHelper;

public class MonthTimeSheetReport {
	
	private Map<String, Map<String, Set<TimeSheet>>> data = new HashMap<String, Map<String, Set<TimeSheet>>>();
	private Map<String, Map<String, Integer>> summary = new HashMap<String, Map<String, Integer>>();
	private List<Date> days;

	public MonthTimeSheetReport() {
		
	}
	
	public Map<String, Map<String, Set<TimeSheet>>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, Set<TimeSheet>>> data) {
		this.data = data;
	}

	public Map<String, Map<String, Integer>> getSummary() {
		return summary;
	}

	public void setSummary(Map<String, Map<String, Integer>> summary) {
		this.summary = summary;
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
				getData().put(key, getDataRows(days));
			}
			getData().get(key).get(DateHelper.getString(ts.getDate())).add(ts);
		}
	}
	
	public void fill(Set<TimeSheet> list, List<Date> days, List<String> holidays) {
		this.setDays(days);
		
		for (TimeSheet ts : list) {
			String key = ts.getEmployee().getEmployeeId().toString();
			// Time Sheet detail data
			if (!getData().containsKey(key)) {
				getData().put(key, getDataRows(days));
			}
			getData().get(key).get(DateHelper.getString(ts.getDate())).add(ts);
			
			// Time Sheet summary data
			if (!getSummary().containsKey(key)) {
				getSummary().put(key, getSummaryRows());
			}
			if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMN_WORK)) {
				if (getSummary().get(key).containsKey(ReportHelper.REPORT_COLUMN_WORK)) {
					getSummary().get(key).put(ReportHelper.REPORT_COLUMN_WORK, getSummary().get(key).get(ReportHelper.REPORT_COLUMN_WORK) + 1);
				}
				else {
					getSummary().get(key).put(ReportHelper.REPORT_COLUMN_WORK, 1);
				}
				
				if (holidays.contains(DateHelper.getString(ts.getDate()))) {
					if (getSummary().get(key).containsKey(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY)) {
						getSummary().get(key).put(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, getSummary().get(key).get(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY) + 1);
					}
					else {
						getSummary().get(key).put(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, 1);
					}
				}
			}
			
			for (String col : ReportHelper.REPORT_COLUMNS_NORMAL.split(",")) {
				if (ReportHelper.isIncludePreference(ts.getAttendanceType(), col)) {
					if (getSummary().get(key).containsKey(col)) {
						getSummary().get(key).put(col, getSummary().get(key).get(col) + 1);
					}
					else {
						getSummary().get(key).put(col, 1);
					}
				}
			}
			
			if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
				if (ReportHelper.isIncludePreference(ts.getEmployee(), ReportHelper.REPORT_COLUMN_ALLOWANCE) || ReportHelper.isIncludePreference(ts.getUnit(), ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
					if (getSummary().get(key).containsKey(ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
						getSummary().get(key).put(ReportHelper.REPORT_COLUMN_ALLOWANCE, getSummary().get(key).get(ReportHelper.REPORT_COLUMN_ALLOWANCE) + 1);
					}
					else {
						getSummary().get(key).put(ReportHelper.REPORT_COLUMN_ALLOWANCE, 1);
					}
				}
			}
		}
	}
	
	private Map<String, Set<TimeSheet>> getDataRows(List<Date> days) {
		Map<String, Set<TimeSheet>> rows = new TreeMap<String, Set<TimeSheet>>();
		for (Date day : days) {
			rows.put(DateHelper.getString(day), new LinkedHashSet<TimeSheet>());
		}
		
		return rows;
	}
	
	private Map<String, Integer> getSummaryRows() {
		Map<String, Integer> rows = new TreeMap<String, Integer>();
		
		return rows;
	}
}
