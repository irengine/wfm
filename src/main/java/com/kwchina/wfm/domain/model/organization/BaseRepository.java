package com.kwchina.wfm.domain.model.organization;

import java.util.List;

public interface BaseRepository<T> {

	List<T> getRows(String whereClause, String orderByClause, int start, int limit);
	Long getRowsCount(String whereClause);

}