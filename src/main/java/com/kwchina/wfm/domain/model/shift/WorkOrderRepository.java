package com.kwchina.wfm.domain.model.shift;

import java.util.Date;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface WorkOrderRepository extends BaseRepository<WorkOrder> {

	int removeAll(Date date);

}
