package com.kwchina.wfm.domain.model.shift;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kwchina.wfm.domain.common.AbstractSpecification;
import com.kwchina.wfm.infrastructure.common.DateHelper;

public class HolidaySpecification extends AbstractSpecification<Date> {

	private List<String> holidays = new ArrayList<String>();
	
	public HolidaySpecification(List<String> holidays) {
		this.holidays = holidays;
	}

	@Override
	public boolean isSatisfiedBy(Date d) {
		if (isHoliday(d))
			return true;
		
		return false;
	}
	
	private boolean isHoliday(Date d) {
		for(String h : holidays) {
			if (DateHelper.getString(d).equals(h)) {
				return true;
			}
		}
		return false;
	}
}
