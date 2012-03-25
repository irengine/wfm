package com.kwchina.wfm.domain.model.organization;

import com.kwchina.wfm.domain.common.BaseRepository;

public interface UserRepository extends BaseRepository<User> {

	User findByCode(String name);
	void disable(User user);

}
