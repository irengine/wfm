package com.kwchina.wfm.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.employee.LeaveEvent;
import com.kwchina.wfm.domain.model.employee.LeaveEventRepository;

@Repository
public class LeaveEventRepositoryImpl extends BaseRepositoryImpl<LeaveEvent> implements LeaveEventRepository {
	
}
