package com.kwchina.wfm.domain.model.shift;

import java.util.List;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface AttendanceTypeRepository extends BaseRepository<AttendanceType> {

	AttendanceType findByName(String name);

	List<AttendanceType> findByProperty(String key, String value);

	void disable(AttendanceType at);
	
}
