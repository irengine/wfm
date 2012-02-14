package com.kwchina.wfm.domain.model.organization;

import java.util.List;

public interface UnitRepository extends BaseRepository<Unit> {
	Unit getRoot(String name);
	Unit findRoot();
	
	List<Unit> findAllChildren(Unit unit);
	List<Unit> findAllAncestor(Unit unit);
	
	void addChild(Unit parentUnit, Unit unit);
	void removeChild(Unit parentUnit, Unit unit);
	
	void printTree(Unit root);
}
