package com.kwchina.wfm.domain.model.organization;

import java.util.List;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface UnitRepository extends BaseRepository<Unit> {
	Unit getRoot(String name);
	Unit findRoot();
	
	Unit findByUriName(String uriName);
	
	List<Unit> findAll();
	List<Unit> findAllChildren(Unit unit);
	List<Unit> findAllAncestor(Unit unit);
	
	void addChild(Unit parentUnit, Unit unit);
	void removeChild(Unit parentUnit, Unit unit);
	
	void disable(Unit unit);
}
