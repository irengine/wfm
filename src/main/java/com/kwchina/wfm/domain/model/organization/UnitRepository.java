package com.kwchina.wfm.domain.model.organization;

import java.util.List;

public interface UnitRepository {
	Unit getRoot(String name);
	Unit findRoot();
	
	List<Unit> findAllChildren(Unit unit);
	List<Unit> findAllAncestor(Unit unit);
	
	void addChild(Unit parentUnit, Unit unit);
	void removeChild(Unit parentUnit, Unit unit);

	Unit findById(Long id);
	Unit findByName(String name);
	List<Unit> findAll();
	
	public void printTree(Node root);
}
