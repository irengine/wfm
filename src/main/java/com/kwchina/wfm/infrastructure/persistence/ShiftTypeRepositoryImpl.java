package com.kwchina.wfm.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.domain.model.shift.CustomShiftPolicy;
import com.kwchina.wfm.domain.model.shift.DailyShiftPolicy;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceFactory;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;

@Repository
public class ShiftTypeRepositoryImpl extends BaseRepositoryImpl<ShiftType> implements ShiftTypeRepository {

//	@PersistenceContext
//	private EntityManager entityManager;
	
	@Autowired
	private SystemPreferenceRepository systemPreferenceRepository;
	
	@Autowired
	private AttendanceTypeRepository attendanceTypeRepository;
	
	@Override
	public CustomShiftPolicy getCustomShiftPolicy(String parameters) {
		String[] ps = StringUtils.split(parameters, ";");
		if (ps.length == 3) {
			Date startDate = DateHelper.getDate(ps[0]);
			int offset = Integer.parseInt(ps[1]);
			String[] ats = StringUtils.split(ps[2], ",");
			List<AttendanceType> attendanceTypes = new ArrayList<AttendanceType>();
			for(String at : ats) {
				attendanceTypes.add(attendanceTypeRepository.findByName(at));
			}
			
			return new CustomShiftPolicy(startDate, offset, attendanceTypes);
		}
		else if (ps.length == 4) {
			Date startDate = DateHelper.getDate(ps[0]);
			int offset = Integer.parseInt(ps[2]);
			String[] ats = StringUtils.split(ps[3], ",");
			List<AttendanceType> attendanceTypes = new ArrayList<AttendanceType>();
			for(String at : ats) {
				attendanceTypes.add(attendanceTypeRepository.findByName(at));
			}
			
			return new CustomShiftPolicy(startDate, offset, attendanceTypes);
		}
		
		return null;
	}

	@Override
	public DailyShiftPolicy getDailyShiftPolicy(String parameters) {
		String[] ps = StringUtils.split(parameters, ";");
		AttendanceType workingDay = attendanceTypeRepository.findByName(ps[0]);
		AttendanceType breakDay = attendanceTypeRepository.findByName(ps[1]);
		String weekends = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getWeekends();
		List<String> holidays = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getHolidays();
		Map<String, String> daysChanged = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getDaysChanged();
		
		return new DailyShiftPolicy(workingDay, breakDay, holidays, weekends, daysChanged);
	}

	@Override
	public int getDailyShiftCount(List<Date> days) {
		AttendanceType workingDay = new AttendanceType();
		AttendanceType breakDay = null;
		String weekends = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getWeekends();
		List<String> holidays = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getHolidays();
		Map<String, String> daysChanged = SystemPreferenceFactory.getInstance(systemPreferenceRepository).getDaysChanged();
		
		DailyShiftPolicy shiftPolicy = new DailyShiftPolicy(workingDay, breakDay, holidays, weekends, daysChanged);
		
		int count = 0;
		for (Date day : days) {
			AttendanceType attendanceType = shiftPolicy.getAttendanceType(day);
			if (null != attendanceType)
				count++;
		}
		
		return count;
	}
}
