package com.kwchina.wfm.domain.model.employee;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface LeaveEventRepository extends BaseRepository<LeaveEvent> {

	void disable(LeaveEvent event);

}
