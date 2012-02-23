package com.kwchina.wfm.domain.model.shift;

import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface SystemPreferenceRepository extends BaseRepository<SystemPreference> {

	void addHoliday(String holiday);
	void removeHoliday(String holiday);
	
	void addDaysChanged(String dayChangedBefore, String dayChangedAfter);
	void removeDaysChanged(String dayChangedBefore, String dayChangedAfter);
	
	List<String> getHolidays();
	Map<String, String> getDaysChanged();
	
	void addAttendanceTypeProperty(String name, String type, String description);
	void removeAttendanceTypeProperty(String name);
	
	List<SystemPreference> getAttendanceTypeProperties();
	
}
