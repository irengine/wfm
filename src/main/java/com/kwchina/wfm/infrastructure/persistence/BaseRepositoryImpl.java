package com.kwchina.wfm.infrastructure.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwchina.wfm.domain.common.BaseRepository;
import com.kwchina.wfm.infrastructure.common.ReflectHelper;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {

	private static final Logger logger = LoggerFactory.getLogger(BaseRepository.class);
			
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
	
	public void save(T t) {
		entityManager.persist(t);
		entityManager.flush();
	}
	
	public void remove(T t) {
		entityManager.remove(t);
		entityManager.flush();
	}
	
	private String getEntityName() {
		return entityClass.getSimpleName();
	}

	public List<T> getRows(String whereClause, String orderByClause, int start, int limit) {
		return getRows(whereClause, orderByClause, start, limit, false);
	}
	
	private String getRowsSyntax(String whereClause, String orderByClause, boolean includeDisabled) {
		String syntax = "";
		
		if(includeDisabled) {
			if (StringUtils.isEmpty(whereClause))
				syntax = String.format("FROM %s %s", getEntityName(), orderByClause);
			else
				syntax = String.format("FROM %s WHERE %s %s", getEntityName(), whereClause, orderByClause);
		}
		else {
			if (StringUtils.isEmpty(whereClause))
				syntax = String.format("FROM %s WHERE enable=true %s", getEntityName(), orderByClause);
			else
				syntax = String.format("FROM %s WHERE enable=true AND %s %s", getEntityName(), whereClause, orderByClause);
		}
		
		logger.debug(syntax);
		
		return syntax;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getRows(String whereClause, String orderByClause, int start, int limit, boolean includeDisabled) {
		Query query = entityManager.createQuery(getRowsSyntax(whereClause, orderByClause, includeDisabled));
		query.setMaxResults(limit);
		query.setFirstResult(start);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getRowsBySql(String sqlSyntax, int start, int limit) {
		Query query = entityManager.createQuery(sqlSyntax);
		query.setMaxResults(limit);
		query.setFirstResult(start);
		return query.getResultList();
	}
	
	public Long getRowsCount(String whereClause) {
		return getRowsCount(whereClause, false);
	}
	
	private String getRowsCountSyntax(String whereClause, boolean includeDisabled) {
		String syntax = "";
		
		if(includeDisabled) {
			if (StringUtils.isEmpty(whereClause))
				syntax = String.format("SELECT COUNT(*) FROM %s", getEntityName());
			else
				syntax = String.format("SELECT COUNT(*) FROM %s WHERE %s", getEntityName(), whereClause);
		}
		else {
			if (StringUtils.isEmpty(whereClause))
				syntax = String.format("SELECT COUNT(*) FROM %s WHERE enable=true", getEntityName());
			else
				syntax = String.format("SELECT COUNT(*) FROM %s WHERE enable=true AND %s", getEntityName(), whereClause);
		}
		return syntax;
	}

	@Override
	public Long getRowsCount(String whereClause, boolean includeDisabled) {
		Long rowsCount = (Long)entityManager.createQuery(getRowsCountSyntax(whereClause, includeDisabled)).getSingleResult(); 
		return rowsCount;
	}
	
	@Override
	public Long getRowsCountBySql(String sqlSyntax) {
		Long rowsCount = (Long)entityManager.createQuery(sqlSyntax).getSingleResult(); 
		return rowsCount;
	}
}
