package com.kwchina.wfm.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;

@Repository
public class AttendanceTypeRepositoryImpl extends BaseRepositoryImpl<AttendanceType> implements AttendanceTypeRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public AttendanceType findByName(String name) {
		try {
			AttendanceType t = (AttendanceType) entityManager.createNamedQuery("attendanceType.findByName")
										.setParameter("name", name)
										.getSingleResult();
			return t;
		}
		catch (NoResultException nre) {
			return null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AttendanceType> findByProperty(String key, String value) {
		
		List<AttendanceType> all = entityManager.createQuery("select at from AttendanceType at where at.enable = true")
				.getResultList();

		List<AttendanceType> ats = new ArrayList<AttendanceType>();
		for (AttendanceType at : all) {
			
			for (Preference p : at.getPreferences())
				if (p.getKey().equals(key) && p.getValue().equals(value)) {
					ats.add(at);
				}
				
		}
		
		return ats;
	}
	
	@Override
	public void disable(AttendanceType at) {
		
		if (at.getId() >= 1000) {
			at.setEnable(false);

			entityManager.persist(at);
			entityManager.flush();
		}
	}

}
