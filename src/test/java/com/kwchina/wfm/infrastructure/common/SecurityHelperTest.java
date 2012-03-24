package com.kwchina.wfm.infrastructure.common;

import static org.junit.Assert.*;

import org.junit.Test;



public class SecurityHelperTest {

	@Test
	public void testSHA1() throws Exception {
		String x = "P@ssword";
		String r = SecurityHelper.encrypt(x);
		System.out.println(r);
		assertEquals("9e7c97801cb4cce87b6c02f98291a6420e6400ad", r);
	}
	
	@Test
	public void testGetCurrentUserName() {
		SecurityHelper.getCurrentUserName();
	}
}
