package com.kwchina.wfm.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository {
	
}
