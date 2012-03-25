package com.kwchina.wfm.interfaces.common;

import com.kwchina.wfm.domain.model.organization.PreferenceGetter;

public class ReportHelper {
	
	public static final String REPORT_COLUMN_WORK = "出勤";
	public static final String REPORT_COLUMN_OVERTIME_HOLIDAY = "节日加班";
	public static final String REPORT_COLUMNS_NORMAL = "工休,产护,探哺,伤计,病事,婚丧,公旷,年调,日,中,夜,全夜";
	public static final String REPORT_COLUMN_ALLOWANCE = "乙种津贴";
	public static final String REPORT_COLUMN_OVERTIME = "延时加班";
	public static final String REPORT_COLUMN_SHIFT = "翻班";
	
	public static final String REPORT_COLUMN_IMPORT_0 = "公休";
	public static final String REPORT_COLUMN_IMPORT_1 = "日班";
	public static final String REPORT_COLUMN_IMPORT_2 = "中班";
	public static final String REPORT_COLUMN_IMPORT_3 = "夜班";
	
	
	public static boolean isIncludePreference(PreferenceGetter pg, String key) {
		if (pg.getPreference(key) == null || pg.getPreference(key).equals("false"))
			return false;
		return true;
	}
}
