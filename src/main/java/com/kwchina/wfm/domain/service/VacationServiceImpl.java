package com.kwchina.wfm.domain.service;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Period;
import org.springframework.stereotype.Component;

import com.kwchina.wfm.domain.model.employee.Employee;

@Component
public class VacationServiceImpl {

	public int calculateVacation(Employee employee, Date calculateAt) {
		DateTime start = new DateTime(employee.getBeginDateOfWork());
		DateTime end = new DateTime(calculateAt);
		Period p = new Period(start, end);
		
		return getDays(p.getYears(), end.get(DateTimeFieldType.monthOfYear()));
	}
	
	private int getDays(int years, int month) {
		int days = 0;
		
		if (years == 0)
			days = 0;
		else if (years == 1)
			days = (5 * (12 - month + 1)) / 12;
		else if (years > 1 && years < 10)
			days = 5;
		else if (years == 10)
			days = 5 * (month - 1) / 12 + 10 * (12 - month + 1) /12 ;
		else if (years > 10 && years < 20)
			days = 10;
		else if (years == 20)
			days = 10 * (month - 1) / 12 + 15 * (12 - month + 1) /12 ;
		else if (years > 20)
			days = 15;
		
		return days;
	}
}
