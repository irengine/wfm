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
			int beginHour = ts.getAttendanceType().getBeginTime() / 60;
			int endHour = ts.getAttendanceType().getEndTime() / 60;
			
			if (!getSummary().containsKey(key)) {
				getSummary().put(key, getSummaryRows());
			}
			
			// Work and holiday
			if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMN_WORK)) {
				// work
				int workQuantity = (endHour - beginHour) / 8;
				setColumn(key, ReportHelper.REPORT_COLUMN_WORK, workQuantity);
//				if (getSummary().get(key).containsKey(ReportHelper.REPORT_COLUMN_WORK)) {
//					getSummary().get(key).put(ReportHelper.REPORT_COLUMN_WORK, getSummary().get(key).get(ReportHelper.REPORT_COLUMN_WORK) + workQuantity);
//				}
//				else {
//					getSummary().get(key).put(ReportHelper.REPORT_COLUMN_WORK, workQuantity);
//				}
				
				// holiday
				int holidayQuantity = 0;
				// 1.yesterday
				if (holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), -1)))) {
					holidayQuantity += ReportHelper.getYesterdayHours(beginHour, endHour) / 8;
				}
				
				// 2.today
				if (holidays.contains(DateHelper.getString(ts.getDate()))) {
					holidayQuantity += ReportHelper.getTodayHours(beginHour, endHour) / 8;
				}
				
				// 3.tomorrow
				if (holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), 1)))) {
					holidayQuantity += ReportHelper.getTomorrowHours(beginHour, endHour) / 8;
				}

				setColumn(key, ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, holidayQuantity);
//				if (getSummary().get(key).containsKey(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY)) {
//					getSummary().get(key).put(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, getSummary().get(key).get(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY) + holidayQuantity);
//				}
//				else {
//					getSummary().get(key).put(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, holidayQuantity);
//				}
			}
			
			// other summary
			for (String col : ReportHelper.REPORT_COLUMNS_NORMAL.split(",")) {
				if (ReportHelper.isIncludePreference(ts.getAttendanceType(), col)) {
					setColumn(key, col, 1);
//					if (getSummary().get(key).containsKey(col)) {
//						getSummary().get(key).put(col, getSummary().get(key).get(col) + 1);
//					}
//					else {
//						getSummary().get(key).put(col, 1);
//					}
				}
			}
			
			// day/middle/night shift and full shift
			// TODO: depend on today and tomorrow
			// special case: full shift
			if (!holidays.contains(DateHelper.getString(ts.getDate())) &&
					!holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), 1))) &&
					ReportHelper.isFullShift(beginHour, endHour)) {
				setColumn(key, ReportHelper.REPORT_COLUMNS_FULL_SHIFT, 1);
//				if (getSummary().get(key).containsKey(ReportHelper.REPORT_COLUMNS_FULL_SHIFT)) {
//					getSummary().get(key).put(ReportHelper.REPORT_COLUMNS_FULL_SHIFT, getSummary().get(key).get(ReportHelper.REPORT_COLUMN_ALLOWANCE) + 1);
//				}
//				else {
//					getSummary().get(key).put(ReportHelper.REPORT_COLUMNS_FULL_SHIFT, 1);
//				}
			}
			else {
				setColumn(key, ReportHelper.REPORT_COLUMNS_FULL_SHIFT, 0);
				setColumn(key, ReportHelper.REPORT_COLUMNS_DAY_SHIFT, 0);
				setColumn(key, ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT, 0);
				setColumn(key, ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT, 0);
				
				// yesterday is not holiday
				if (!holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), -1)))) {
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_DAY_SHIFT) && ReportHelper.isDayShift(beginHour, endHour, -24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_DAY_SHIFT, 1);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT) && ReportHelper.isMiddleShift(beginHour, endHour, -24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT, 1);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT) && ReportHelper.isNightShift(beginHour, endHour, -24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT, 1);
				}

				// today is not holdliday
				if (!holidays.contains(DateHelper.getString(ts.getDate()))) {
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_DAY_SHIFT) && ReportHelper.isDayShift(beginHour, endHour, 0))
						setColumn(key, ReportHelper.REPORT_COLUMNS_DAY_SHIFT, 1);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT) && ReportHelper.isMiddleShift(beginHour, endHour, 0))
						setColumn(key, ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT, 1);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT) && ReportHelper.isNightShift(beginHour, endHour, 0))
						setColumn(key, ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT, 1);
				}
				
				// tomorrow is not holiday
				if (!holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), 1)))) {
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_DAY_SHIFT) && ReportHelper.isDayShift(beginHour, endHour, 24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_DAY_SHIFT, 1);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT) && ReportHelper.isMiddleShift(beginHour, endHour, 24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT, 1);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT) && ReportHelper.isNightShift(beginHour, endHour, 24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT, 1);
				}
			}
			
			if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
				if (ReportHelper.isIncludePreference(ts.getEmployee(), ReportHelper.REPORT_COLUMN_ALLOWANCE)
						|| ReportHelper.isIncludePreference(ts.getUnit(), ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
					setColumn(key, ReportHelper.REPORT_COLUMN_ALLOWANCE, 1);
//					if (getSummary().get(key).containsKey(ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
//						getSummary().get(key).put(ReportHelper.REPORT_COLUMN_ALLOWANCE, getSummary().get(key).get(ReportHelper.REPORT_COLUMN_ALLOWANCE) + 1);
//					}
//					else {
//						getSummary().get(key).put(ReportHelper.REPORT_COLUMN_ALLOWANCE, 1);
//					}
				}
			}
		}
	}
	
	private void setColumn(String key, String col, int val) {
		if (getSummary().get(key).containsKey(col)) {
			getSummary().get(key).put(col, getSummary().get(key).get(col) + val);
		}
		else {
			getSummary().get(key).put(col, 1);
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
		
		rows.put(ReportHelper.REPORT_COLUMN_WORK, 0);
		rows.put(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, 0);
		for(String s : ReportHelper.REPORT_COLUMNS_NORMAL.split(","))
			rows.put(s, 0);
		
		rows.put(ReportHelper.REPORT_COLUMN_ALLOWANCE, 0);
		
		return rows;
	}
}
