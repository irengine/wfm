package com.kwchina.wfm.domain.common;

import java.util.List;

public interface BaseRepository<T> {

	T findById(Long id);
	
	T save(T t);

	List<T> getRows(String whereClause, String orderByClause, int start, int limit, boolean includeDisabled);
	Long getRowsCount(String whereClause, boolean includeDisabled);

	List<T> getRows(String whereClause, String orderByClause, int start, int limit);
	Long getRowsCount(String whereClause);
}