package com.kwchina.wfm.infrastructure.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.shift.SystemPreference;
import com.kwchina.wfm.domain.model.shift.SystemPreference.ScopeType;
import com.kwchina.wfm.domain.model.shift.SystemPreferenceRepository;

@Repository
public class SystemPreferenceRepositoryImpl extends BaseRepositoryImpl<SystemPreference> implements SystemPreferenceRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void addHoliday(String holiday) {
		SystemPreference p = new SystemPreference(SystemPreference.ScopeType.HOLIDAY, holiday, null, null);
		entityManager.persist(p);
		entityManager.flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void removeHoliday(String holiday) {
		List<SystemPreference> ps = entityManager.createNamedQuery("systemPreference.findByScopeAndKey")
										.setParameter("scope", ScopeType.HOLIDAY)
										.setParameter("key", holiday)
										.getResultList();
		for(SystemPreference p : ps) {
			entityManager.remove(p);
		}
		entityManager.flush();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getHolidays() {
		List<String> holidays = new ArrayList<String>();
		List<SystemPreference> ps = entityManager.createNamedQuery("systemPreference.findByScope")
										.setParameter("scope", ScopeType.HOLIDAY)
										.getResultList();
		for(SystemPreference p : ps) {
			holidays.add(p.getKey());
		}
		return holidays;
	}

	@Override
	public void addDaysChanged(String dayChangedBefore, String dayChangedAfter) {
		SystemPreference before = new SystemPreference(SystemPreference.ScopeType.DAYCHANGED, dayChangedBefore, null, dayChangedAfter);
		SystemPreference after = new SystemPreference(SystemPreference.ScopeType.DAYCHANGED, dayChangedAfter, null, dayChangedBefore);
		entityManager.persist(before);
		entityManager.persist(after);
		entityManager.flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void removeDaysChanged(String dayChangedBefore, String dayChangedAfter) {
		List<SystemPreference> psBefore = entityManager.createNamedQuery("systemPreference.findByScopeAndKey")
										.setParameter("scope", ScopeType.DAYCHANGED)
										.setParameter("key", dayChangedBefore)
										.getResultList();
		for(SystemPreference p : psBefore) {
			entityManager.remove(p);
		}
		
		List<SystemPreference> psAfter = entityManager.createNamedQuery("systemPreference.findByScopeAndKey")
										.setParameter("scope", ScopeType.DAYCHANGED)
										.setParameter("key", dayChangedAfter)
										.getResultList();
		for(SystemPreference p : psAfter) {
			entityManager.remove(p);
		}
		
		entityManager.flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> getDaysChanged() {
		Map<String, String> daysChanged = new HashMap<String, String>();
		List<SystemPreference> ps = entityManager.createNamedQuery("systemPreference.findByScope")
										.setParameter("scope", ScopeType.DAYCHANGED)
										.getResultList();
		for(SystemPreference p : ps) {
			daysChanged.put(p.getKey(), p.getValue());
		}
		return daysChanged;
	}

	@Override
	public void addAttendanceTypeProperty(String name, String type, String description) {
		SystemPreference p = new SystemPreference(SystemPreference.ScopeType.ATTENDANCETYPE, name, type, description);
		entityManager.persist(p);
		entityManager.flush();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void removeAttendanceTypeProperty(String name) {
		List<SystemPreference> ps = entityManager.createNamedQuery("systemPreference.findByScopeAndKey")
										.setParameter("scope", ScopeType.ATTENDANCETYPE)
										.setParameter("key", name)
										.getResultList();
		for(SystemPreference p : ps) {
			entityManager.remove(p);
		}
		entityManager.flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SystemPreference> getAttendanceTypeProperties() {
		return entityManager.createNamedQuery("systemPreference.findByScope")
				.setParameter("scope", ScopeType.ATTENDANCETYPE)
				.getResultList();
	}
}
