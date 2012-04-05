package com.kwchina.wfm.domain.model.shift;

import java.util.Date;
import java.util.List;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface ShiftTypeRepository extends BaseRepository<ShiftType> {

	CustomShiftPolicy getCustomShiftPolicy(String parameters);
	DailyShiftPolicy getDailyShiftPolicy(String parameters);
	int getDailyShiftCount(List<Date> days);
	
}
