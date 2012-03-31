package com.kwchina.wfm.interfaces.report;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.common.ReportHelper;
import com.kwchina.wfm.interfaces.organization.web.SystemController;

public class MonthTimeSheetReport {

	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

	
	private Map<String, Map<String, Set<TimeSheet>>> data = new HashMap<String, Map<String, Set<TimeSheet>>>();
	private Map<String, Map<String, Float>> summary = new HashMap<String, Map<String, Float>>();
	private List<Date> days;

	public MonthTimeSheetReport() {
		
	}
	
	public Map<String, Map<String, Set<TimeSheet>>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, Set<TimeSheet>>> data) {
		this.data = data;
	}

	public Map<String, Map<String, Float>> getSummary() {
		return summary;
	}

	public void setSummary(Map<String, Map<String, Float>> summary) {
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
				Float workQuantity = (endHour - beginHour) / 8f;
				setColumn(key, ReportHelper.REPORT_COLUMN_WORK, workQuantity);
				
				// holiday
				Float holidayQuantity = 0f;
				
				logger.debug("yesterday hours {}", ReportHelper.getYesterdayHours(beginHour, endHour));
				logger.debug("today hours {}", ReportHelper.getTodayHours(beginHour, endHour));
				logger.debug("tomorrow hours {}", ReportHelper.getTomorrowHours(beginHour, endHour));
				
				// 1.yesterday
				if (holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), -1)))) {
					holidayQuantity += ReportHelper.getYesterdayHours(beginHour, endHour) / 8f;
				}
				
				// 2.today
				if (holidays.contains(DateHelper.getString(ts.getDate()))) {
					
					holidayQuantity += ReportHelper.getTodayHours(beginHour, endHour) / 8f;
				}
				
				// 3.tomorrow
				if (holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), 1)))) {
					holidayQuantity += ReportHelper.getTomorrowHours(beginHour, endHour) / 8f;
				}

				setColumn(key, ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, holidayQuantity);
			}
			
			// other summary
			for (String col : ReportHelper.REPORT_COLUMNS_NORMAL.split(",")) {
				if (ReportHelper.isIncludePreference(ts.getAttendanceType(), col)) {
					setColumn(key, col, 1f);
				}
			}
			
			// day/middle/night shift and full shift
			// TODO: depend on today and tomorrow
			// special case: full shift
			if (!holidays.contains(DateHelper.getString(ts.getDate())) &&
					!holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), 1))) &&
					ReportHelper.isFullShift(beginHour, endHour) && 
					ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_DAY_SHIFT) &&
					ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT) &&
					ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT)) {
				setColumn(key, ReportHelper.REPORT_COLUMNS_FULL_SHIFT, 1f);
			}
			else {
				setColumn(key, ReportHelper.REPORT_COLUMNS_FULL_SHIFT, 0f);
				setColumn(key, ReportHelper.REPORT_COLUMNS_DAY_SHIFT, 0f);
				setColumn(key, ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT, 0f);
				setColumn(key, ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT, 0f);
				
				// yesterday is not holiday
				if (!holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), -1)))) {
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_DAY_SHIFT) && ReportHelper.isDayShift(beginHour, endHour, -24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_DAY_SHIFT, 1f);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT) && ReportHelper.isMiddleShift(beginHour, endHour, -24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT, 1f);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT) && ReportHelper.isNightShift(beginHour, endHour, -24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT, 1f);
				}

				// today is not holiday
				if (!holidays.contains(DateHelper.getString(ts.getDate()))) {
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_DAY_SHIFT) && ReportHelper.isDayShift(beginHour, endHour, 0))
						setColumn(key, ReportHelper.REPORT_COLUMNS_DAY_SHIFT, 1f);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT) && ReportHelper.isMiddleShift(beginHour, endHour, 0))
						setColumn(key, ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT, 1f);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT) && ReportHelper.isNightShift(beginHour, endHour, 0))
						setColumn(key, ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT, 1f);
				}
				
				// tomorrow is not holiday
				if (!holidays.contains(DateHelper.getString(DateHelper.addDay(ts.getDate(), 1)))) {
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_DAY_SHIFT) && ReportHelper.isDayShift(beginHour, endHour, 24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_DAY_SHIFT, 1f);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT) && ReportHelper.isMiddleShift(beginHour, endHour, 24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_MIDDLE_SHIFT, 1f);
					if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT) && ReportHelper.isNightShift(beginHour, endHour, 24))
						setColumn(key, ReportHelper.REPORT_COLUMNS_NIGHT_SHIFT, 1f);
				}
			}
			
			if (ReportHelper.isIncludePreference(ts.getAttendanceType(), ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
				if (ReportHelper.isIncludePreference(ts.getEmployee(), ReportHelper.REPORT_COLUMN_ALLOWANCE)
						|| ReportHelper.isIncludePreference(ts.getUnit(), ReportHelper.REPORT_COLUMN_ALLOWANCE)) {
					
					Float workQuantity = (endHour - beginHour) / 8f;
					setColumn(key, ReportHelper.REPORT_COLUMN_ALLOWANCE, workQuantity);
				}
			}
		}
	}
	
	private void setColumn(String key, String col, Float val) {
		if (getSummary().get(key).containsKey(col)) {
			getSummary().get(key).put(col, getSummary().get(key).get(col) + val);
		}
		else {
			getSummary().get(key).put(col, val);
		}
	}
	
	private Map<String, Set<TimeSheet>> getDataRows(List<Date> days) {
		Map<String, Set<TimeSheet>> rows = new TreeMap<String, Set<TimeSheet>>();
		for (Date day : days) {
			rows.put(DateHelper.getString(day), new LinkedHashSet<TimeSheet>());
		}
		
		return rows;
	}
	
	private Map<String, Float> getSummaryRows() {
		Map<String, Float> rows = new TreeMap<String, Float>();
		
		rows.put(ReportHelper.REPORT_COLUMN_WORK, 0f);
		rows.put(ReportHelper.REPORT_COLUMN_OVERTIME_HOLIDAY, 0f);
		for(String s : ReportHelper.REPORT_COLUMNS_NORMAL.split(","))
			rows.put(s, 0f);
		
		rows.put(ReportHelper.REPORT_COLUMN_ALLOWANCE, 0f);
		
		return rows;
	}
}
