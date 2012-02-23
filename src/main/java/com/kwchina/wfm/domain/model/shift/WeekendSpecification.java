package com.kwchina.wfm.domain.model.shift;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.common.AbstractSpecification;
import com.kwchina.wfm.infrastructure.common.DateHelper;

public class WeekendSpecification extends AbstractSpecification<Date> {
	
	/*
	 * SUNDAY = 1
	 * SATURDAY = 7
	 */
	private List<Integer> weekends = new ArrayList<Integer>();
	private Map<String, String> daysChanged = new HashMap<String, String>();
	
	public WeekendSpecification(String weekends, Map<String, String> daysChanged) {

		String[] days = weekends.split(",");
		for(String day : days) {
			this.weekends.add(Integer.parseInt(day));
		}
		this.daysChanged = daysChanged;
	}

	@Override
	public boolean isSatisfiedBy(Date d) {
		if (daysChanged.containsKey(DateHelper.getString(d))) {
			if (isWeekend(DateHelper.getDate(daysChanged.get(DateHelper.getString(d))))) {
				return true;
			}
			return false;
		}
		
		if (isWeekend(d))
			return true;
		
		return false;
	}
	
	private boolean isWeekend(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		
		for(int w : weekends) {
			if (w == dayOfWeek)
				return true;
		}
		
		return false;
	}

}
