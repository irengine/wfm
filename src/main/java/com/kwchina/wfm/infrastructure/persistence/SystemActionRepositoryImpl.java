package com.kwchina.wfm.infrastructure.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.system.SystemAction;
import com.kwchina.wfm.domain.model.system.SystemActionRepository;

@Repository
public class SystemActionRepositoryImpl extends BaseRepositoryImpl<SystemAction> implements SystemActionRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addAction(SystemAction.ScopeType scope, String key, String type, String message) {
		
		SystemAction p = new SystemAction(scope, key, type, message);
		entityManager.persist(p);
		entityManager.flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void removeAction(SystemAction.ScopeType scope, String key) {
		
		List<SystemAction> ps = entityManager.createNamedQuery("systemAction.findByScopeAndKey")
										.setParameter("scope", scope)
										.setParameter("key", key)
										.getResultList();
		for(SystemAction p : ps) {
			entityManager.remove(p);
		}
		
		entityManager.flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SystemAction> getActions(SystemAction.ScopeType scope) {

		List<SystemAction> ps = entityManager.createNamedQuery("systemAction.findByScope")
				.setParameter("scope", scope)
				.getResultList();
		
		return ps;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SystemAction> getActions(SystemAction.ScopeType scope, String key) {

		List<SystemAction> ps = entityManager.createNamedQuery("systemAction.findByScopeAndKey")
				.setParameter("scope", scope)
				.setParameter("key", key)
				.getResultList();
		
		return ps;
	}
}
