package com.kwchina.wfm.infrastructure.common;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encodePassword(String rawPass, Object salt)
			throws DataAccessException {
		
		return SecurityHelper.encrypt(rawPass);
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt)
			throws DataAccessException {
		
		return encPass.equals(SecurityHelper.encrypt(rawPass));
	}

}
