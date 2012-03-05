package com.kwchina.wfm.infrastructure.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void disable(User user) {
	
		user.setEnable(false);

		entityManager.persist(user);
		entityManager.flush();
	}
}
