package com.kwchina.wfm.domain.model.employee;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface JobChangeEventRepository extends BaseRepository<JobChangeEvent> {

	void addEvent(JobChangeEvent event);

}
