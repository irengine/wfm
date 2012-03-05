package com.kwchina.wfm.infrastructure.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;

@Repository
public class EmployeeRepositoryImpl extends BaseRepositoryImpl<Employee> implements EmployeeRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void disable(Employee employee) {
	
		employee.setEnable(false);

		entityManager.persist(employee);
		entityManager.flush();
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> findByUnitId(Long unitId) {
		List<Employee> employees = entityManager.createNamedQuery("employee.findByUnitId")
								.setParameter("unitId", unitId)
								.getResultList();
		return employees;
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> findAllByUnitId(Long unitId) {
		List<Employee> employees = entityManager.createNamedQuery("employee.findAllByUnitId")
								.setParameter("unitId", unitId)
								.getResultList();
		return employees;
	}
}
