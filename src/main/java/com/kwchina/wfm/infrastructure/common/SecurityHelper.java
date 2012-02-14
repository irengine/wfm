package com.kwchina.wfm.infrastructure.common;

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
}
