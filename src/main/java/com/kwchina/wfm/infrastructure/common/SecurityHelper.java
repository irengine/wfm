package com.kwchina.wfm.infrastructure.common;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;

public class SecurityHelper {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static String encrypt(String x) {
		try {
			java.security.MessageDigest d = null;
			d = java.security.MessageDigest.getInstance("SHA-1");
			d.reset();
			d.update(x.getBytes());
			
			return byteArrayToHexString(d.digest());
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public static String getCurrentUserName() {
		
		if (null == SecurityContextHolder.getContext().getAuthentication()) {
			return "n/a";
		}
		else {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDetails userDetails = null;
			if (principal instanceof UserDetails) {
			  userDetails = (UserDetails) principal;
			  if (null != userDetails) {
				  User u = getUserByCode(userDetails.getUsername());
				  
				  if (null != u)
					  return u.getName();
			  }
			}
		}

		return null;
	}
	
	public static User getCurrentUser() {
		
		if (null != SecurityContextHolder.getContext().getAuthentication()) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDetails userDetails = null;
			if (principal instanceof UserDetails) {
			  userDetails = (UserDetails) principal;
			  if (null != userDetails) {
				  User u = getUserByCode(userDetails.getUsername());
				  return u;
			  }
			}
		}

		return null;
	}
	
	private static User getUserByCode(String code) {
		ApplicationContext context = ApplicationContextProvider.getApplicationContext();
		UserRepository ur = context.getBean(com.kwchina.wfm.domain.model.organization.UserRepository.class);
		User u = ur.findByCode(code);
		return u;
	}
}
