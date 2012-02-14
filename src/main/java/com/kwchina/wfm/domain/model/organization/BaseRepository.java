package com.kwchina.wfm.domain.model.organization;

import java.util.List;

public interface BaseRepository<T> {

	T findById(Long id);
	
	T save(T t);

	List<T> getRows(String whereClause, String orderByClause, int start, int limit, boolean hasDisabled);
	Long getRowsCount(String whereClause, boolean hasDisabled);

	List<T> getRows(String whereClause, String orderByClause, int start, int limit);
	Long getRowsCount(String whereClause);
}