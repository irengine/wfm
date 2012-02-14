package com.kwchina.wfm.infrastructure.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.kwchina.wfm.domain.model.organization.BaseRepository;
import com.kwchina.wfm.infrastructure.common.ReflectHelper;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {

	@PersistenceContext
	private EntityManager entityManager;

	private Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseRepositoryImpl() {
		this.entityClass = ReflectHelper.getSuperClassGenricType(this.getClass());
	}
	
	public T findById(Long id) {
		return entityManager.find(entityClass, id);
	}
	
	public T save(T t) {
		entityManager.persist(t);
		entityManager.flush();
		
		return t;
	}
	
	private String getEntityName() {
		return entityClass.getSimpleName();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getRows(String whereClause, String orderByClause, int start, int limit) {
		String syntax = "";
		
		if (whereClause.isEmpty())
			syntax = String.format("FROM %s %s", getEntityName(), orderByClause);
		else
			syntax = String.format("FROM %s WHERE %s %s", getEntityName(), whereClause, orderByClause);
			
		
		Query query = entityManager.createQuery(syntax);
		query.setMaxResults(limit);
		query.setFirstResult(start);
		return query.getResultList();	}

	@Override
	public Long getRowsCount(String whereClause) {
		String syntax = "";
		
		if (whereClause.isEmpty())
			syntax = String.format("SELECT COUNT(*) FROM %s", getEntityName());
		else
			syntax = String.format("SELECT COUNT(*) FROM %s WHERE %s", getEntityName(), whereClause);
		
		Long rowsCount = (Long)entityManager.createQuery(syntax).getSingleResult(); 
		return rowsCount;
	}
}
