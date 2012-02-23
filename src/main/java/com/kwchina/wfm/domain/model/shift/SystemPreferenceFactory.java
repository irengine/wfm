package com.kwchina.wfm.domain.model.shift;

import java.util.List;
import java.util.Map;

public class SystemPreferenceFactory {
	
	private static SystemPreferenceFactory factory = null;
	
	private SystemPreferenceRepository systemPreferenceRepository;
	
	private SystemPreferenceFactory(SystemPreferenceRepository systemPreferenceRepository) {
		this.systemPreferenceRepository = systemPreferenceRepository;
	}

	public static SystemPreferenceFactory getInstance(SystemPreferenceRepository systemPreferenceRepository) {
		if (null == factory)
			factory = new SystemPreferenceFactory(systemPreferenceRepository);
		
		return factory;
	}
	
	public List<String> getHolidays() {
		return systemPreferenceRepository.getHolidays();
	}
	
	public Map<String, String> getDaysChanged() {
		return systemPreferenceRepository.getDaysChanged();
	}
	
	public String getWeekends() {
		return "1,7";
	}
}
