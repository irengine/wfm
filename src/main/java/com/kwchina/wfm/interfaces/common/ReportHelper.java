package com.kwchina.wfm.interfaces.common;

import com.kwchina.wfm.domain.model.organization.PreferenceGetter;

public class ReportHelper {
	
	public static final String REPORT_COLUMN_EXPECTED_WORK = "应出勤";
	public static final String REPORT_COLUMN_WORK = "出勤";
	public static final String REPORT_COLUMN_OVERTIME_HOLIDAY = "节日加班";
	public static final String REPORT_COLUMNS_ABSENT = "工休,产,护,探,哺,伤,计,病,事,婚,丧,公,旷,年,调";
	public static final String REPORT_COLUMNS_DAY_SHIFT = "日";
	public static final String REPORT_COLUMNS_MIDDLE_SHIFT = "中";
	public static final String REPORT_COLUMNS_NIGHT_SHIFT = "夜";
	public static final String REPORT_COLUMNS_FULL_SHIFT = "全夜";
	public static final String REPORT_COLUMN_ALLOWANCE = "乙种津贴";

	public static final String REPORT_COLUMN_OVERTIME = "延时加班";
	public static final String REPORT_COLUMN_SHIFT = "翻班";
	
	public static final String REPORT_COLUMN_IMPORT_0 = "工休";
	public static final String REPORT_COLUMN_IMPORT_1 = "日班";
	public static final String REPORT_COLUMN_IMPORT_2 = "中班";
	public static final String REPORT_COLUMN_IMPORT_3 = "夜班";
	
	
	public static boolean isIncludePreference(PreferenceGetter pg, String key) {
		if (pg.getPreference(key) == null || pg.getPreference(key).equals("false"))
			return false;
		return true;
	}
	
	public static int getYesterdayHours(int beginHour, int endHour) {
		
		return getHours(beginHour, endHour, -24, 0);
	}
	
	public static int getTodayHours(int beginHour, int endHour) {
		
		return getHours(beginHour, endHour, 0, 24);
	}
	
	public static int getTomorrowHours(int beginHour, int endHour) {
		
		return getHours(beginHour, endHour, 24, 48);
	}
	
	private static int getHours(int beginHour, int endHour, int scopeBeginHour, int scopeEndHour) {
		
		if (endHour <= scopeBeginHour || beginHour >= scopeEndHour)
			return 0;
		
		return (endHour > scopeEndHour ? scopeEndHour : endHour) - (beginHour < scopeBeginHour ? scopeBeginHour : beginHour);
	}
	
	public static boolean isFullShift(int beginHour, int endHour) {
		return (endHour - beginHour) == 24;
	}
	
	public static boolean isDayShift(int beginHour, int endHour, int offset) {
		
		return isShift(beginHour, endHour, 8 + offset, 16 + offset);
	}

	public static boolean isMiddleShift(int beginHour, int endHour, int offset) {
		
		return isShift(beginHour, endHour, 16 + offset, 24 + offset);
	}

	public static boolean isNightShift(int beginHour, int endHour, int offset) {
		
		return isShift(beginHour, endHour, 0 + offset, 8 + offset);
	}
	
	private static boolean isShift(int beginHour, int endHour, int scopeBeginHour, int scopeEndHour) {
		
		return (beginHour >= scopeBeginHour && beginHour < scopeEndHour)
				|| (endHour > scopeBeginHour && endHour <= scopeEndHour)
				|| (beginHour <= scopeBeginHour && endHour >= scopeEndHour);
	}
}
