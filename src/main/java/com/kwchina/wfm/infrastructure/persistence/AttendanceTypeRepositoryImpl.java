package com.kwchina.wfm.infrastructure.persistence;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

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

}
