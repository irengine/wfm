package com.kwchina.wfm.infrastructure.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.employee.JobChangeEvent;
import com.kwchina.wfm.domain.model.employee.JobChangeEventRepository;

@Repository
public class JobChangeEventRepositoryImpl extends BaseRepositoryImpl<JobChangeEvent> implements JobChangeEventRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public void addEvent(JobChangeEvent event) {
		List<JobChangeEvent> ps = entityManager.createNamedQuery("jobChangeEvent.findLast")
				.setParameter("employee", event.getEmployee())
				.getResultList();
		for(JobChangeEvent p : ps) {
			p.setEndDate(event.getBeginDate());
			entityManager.persist(p);
		}

		entityManager.persist(event);
		entityManager.flush();
		
	}
}
