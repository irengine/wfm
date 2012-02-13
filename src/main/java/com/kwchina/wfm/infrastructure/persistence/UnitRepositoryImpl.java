package com.kwchina.wfm.infrastructure.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;

@Repository
public class UnitRepositoryImpl implements UnitRepository {
	
	private Log log = LogFactory.getLog(Unit.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void printTree(Unit root)
	{
		log.debug("--" + root.getName());
		log.debug("n = " + root.getId());
		log.debug("left = " + root.getLeft());
		log.debug("right = " + root.getRight());
		log.debug("parent = " + (null == root.getParent() ? 0 : root.getParent().getId()));
		
		for(Unit n : root.getChildren())
		{
			printTree(n);
		}
	}
	
	public Unit getRoot(String name)
	{
		Unit root = findRoot();
		if (null == root)
			root = createRoot(name);
		
		return root;
	}
	
	public Unit findRoot() {
		try {
			Unit unit = (Unit) entityManager.createNamedQuery("unit.findRoot")
					.getSingleResult();
			return unit;
		}
		catch (NoResultException nre) {
			return null;
		}
	}
	
	private Unit createRoot(String name) {
		Unit unit = new Unit(name);
		unit.setParent(null);
		entityManager.persist(unit);
		entityManager.flush();
		
		updateLRValue(unit);
		
		return unit;
	}
	
	public void addChild(Unit parentUnit, Unit unit) {
		parentUnit.addChild(unit);
		entityManager.persist(parentUnit);
		entityManager.flush();
		
		updateLRValue(unit);
	}

	public void removeChild(Unit parentUnit, Unit unit) {
		parentUnit.removeChild(unit);
		entityManager.persist(parentUnit);
		entityManager.flush();
		
		updateLRValue(unit);
	}
	
	private void updateLRValue(Unit unit)
	{
		Long parentLeft = null == unit.getParent() ? 0 : unit.getParent().getLeft();
		
		int cnt = 0;
		cnt = entityManager.createQuery("UPDATE Unit SET leftId = leftId + 2 WHERE leftId > :parentLeft").
				setParameter("parentLeft", parentLeft)
				.executeUpdate();
		log.debug("1.Set existing objects left hand bounds = " + cnt);
		
		cnt = entityManager.createQuery("UPDATE Unit SET leftId = :parentLeft WHERE leftId = 0").
				setParameter("parentLeft", parentLeft + 1)
				.executeUpdate();
		log.debug("2.Set new objects right hand bounds = " + cnt);
		
		cnt = entityManager.createQuery("UPDATE Unit SET rightId = rightId + 2 WHERE rightId > :parentLeft").
				setParameter("parentLeft", parentLeft)
				.executeUpdate();
		log.debug("3.Set existing objects right hand bounds = " + cnt);
		
		cnt = entityManager.createQuery("UPDATE Unit SET rightId = :parentLeft WHERE rightId = 0").
				setParameter("parentLeft", parentLeft + 2)
				.executeUpdate();
		log.debug("4.Set new objects right hand bounds = " + cnt);
		
		// refresh root
		Unit root = findRoot();
		entityManager.refresh(root);
	}
	
	public Unit findById(Long id) {
		return entityManager.find(Unit.class, id);
	}
	
	public Unit findByName(String name) {
		Unit unit = (Unit) entityManager.createNamedQuery("unit.findByName")
								.setParameter("name", name)
								.getSingleResult();
		return unit;
	}

	@SuppressWarnings("unchecked")
	public List<Unit> findAll() {
		List<Unit> units = entityManager.createNamedQuery("unit.findAll")
								.getResultList();
		return units;
	}

	@SuppressWarnings("unchecked")
	public List<Unit> findAllChildren(Unit unit) {
		List<Unit> units = entityManager.createNamedQuery("unit.findAllChildren")
								.setParameter("parentLeft", unit.getLeft())
								.setParameter("parentRight", unit.getRight())
								.getResultList();
		return units;
	}
	
	@SuppressWarnings("unchecked")
	public List<Unit> findAllAncestor(Unit unit) {
		List<Unit> units = entityManager.createNamedQuery("unit.findAllAncestor")
								.setParameter("leafLeft", unit.getLeft())
								.setParameter("leafRight", unit.getRight())
								.getResultList();
		return units;
	}
	
	public Long getRowsCount(String whereClause) {
		
		String syntax = "";
		
		if (whereClause.isEmpty())
			syntax = "SELECT COUNT(*) FROM Unit";
		else
			syntax = String.format("SELECT COUNT(*) FROM Unit WHERE %s", whereClause);
		
		Long rowsCount = (Long)entityManager.createQuery(syntax).getSingleResult(); 
		return rowsCount;
	}
	
	@SuppressWarnings("unchecked")
	public List<Unit> getRows(String whereClause, String orderByClause, int start, int limit) {
		String syntax = "";
		
		if (whereClause.isEmpty())
			syntax = String.format("FROM Unit %s", orderByClause);
		else
			syntax = String.format("FROM Unit WHERE %s %s", whereClause, orderByClause);
			
		
		Query query = entityManager.createQuery(syntax);
		query.setMaxResults(limit);
		query.setFirstResult(start);
		return query.getResultList();
	}
}
