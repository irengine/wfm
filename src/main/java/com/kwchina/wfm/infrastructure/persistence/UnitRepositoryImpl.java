package com.kwchina.wfm.infrastructure.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;

@Repository
public class UnitRepositoryImpl extends BaseRepositoryImpl<Unit> implements UnitRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(UnitRepository.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Unit getRoot(String name)
	{
		Unit root = findRoot();
		if (null == root)
			root = createRoot(name);
		
		return root;
	}
	
	@Override
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
	
	@Override
	public void addChild(Unit parentUnit, Unit unit) {
		parentUnit.addChild(unit);
		entityManager.persist(parentUnit);
		entityManager.flush();
		
		updateLRValue(unit);
	}

	@Override
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
		logger.debug("1.Set existing objects left hand bounds = " + cnt);
		
		cnt = entityManager.createQuery("UPDATE Unit SET leftId = :parentLeft WHERE leftId = 0").
				setParameter("parentLeft", parentLeft + 1)
				.executeUpdate();
		logger.debug("2.Set new objects right hand bounds = " + cnt);
		
		cnt = entityManager.createQuery("UPDATE Unit SET rightId = rightId + 2 WHERE rightId > :parentLeft").
				setParameter("parentLeft", parentLeft)
				.executeUpdate();
		logger.debug("3.Set existing objects right hand bounds = " + cnt);
		
		cnt = entityManager.createQuery("UPDATE Unit SET rightId = :parentLeft WHERE rightId = 0").
				setParameter("parentLeft", parentLeft + 2)
				.executeUpdate();
		logger.debug("4.Set new objects right hand bounds = " + cnt);
		
		// refresh root
		Unit root = findRoot();
		entityManager.refresh(root);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Unit> findAllChildren(Unit unit) {
		List<Unit> units = entityManager.createNamedQuery("unit.findAllChildren")
								.setParameter("parentLeft", unit.getLeft())
								.setParameter("parentRight", unit.getRight())
								.getResultList();
		return units;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Unit> findAllAncestor(Unit unit) {
		List<Unit> units = entityManager.createNamedQuery("unit.findAllAncestor")
								.setParameter("leafLeft", unit.getLeft())
								.setParameter("leafRight", unit.getRight())
								.getResultList();
		return units;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Unit> findAll() {
		List<Unit> units = entityManager.createNamedQuery("unit.findAll")
								.getResultList();
		return units;
	}
	
	@Override
	public void disable(Unit unit) {
	
		unit.setEnable(false);

		entityManager.persist(unit);
		entityManager.flush();
	}
}
