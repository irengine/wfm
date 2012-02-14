package com.kwchina.wfm.infrastructure.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.organization.Employee;
import com.kwchina.wfm.domain.model.organization.EmployeeRepository;

@Repository
public class EmployeeRepositoryImpl extends BaseRepositoryImpl<Employee> implements EmployeeRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeRepository.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public Employee disable(Employee employee) {
		logger.info("delete/disable employee");
		
		employee.setEnable(false);

		entityManager.persist(employee);
		entityManager.flush();
		
		return employee;
	}
}
