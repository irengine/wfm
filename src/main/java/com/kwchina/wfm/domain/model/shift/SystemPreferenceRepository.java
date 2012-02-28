package com.kwchina.wfm.domain.model.shift;

import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface SystemPreferenceRepository extends BaseRepository<SystemPreference> {

	void addHoliday(String holiday);
	void removeHoliday(String holiday);
	
	void addDaysChanged(String dayChangedBefore, String dayChangedAfter);
	void removeDaysChanged(String dayChanged);
	
	List<String> getHolidays();
	Map<String, String> getDaysChanged();
	
	void addProperty(SystemPreference.ScopeType scope, String name, String type, String description);
	void removeProperty(SystemPreference.ScopeType scope, String name);
	List<SystemPreference> getProperties(SystemPreference.ScopeType scope);
	
}
