package com.kwchina.wfm.domain.model.shift;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface AttendanceTypeRepository extends BaseRepository<AttendanceType> {

	AttendanceType findByName(String name);
	
}
