package com.kwchina.wfm.infrastructure.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.employee.LeaveEvent;
import com.kwchina.wfm.domain.model.employee.LeaveEventRepository;

@Repository
public class LeaveEventRepositoryImpl extends BaseRepositoryImpl<LeaveEvent> implements LeaveEventRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void disable(LeaveEvent event) {
		
		event.setEnable(false);

		entityManager.persist(event);
		entityManager.flush();
	}
}
