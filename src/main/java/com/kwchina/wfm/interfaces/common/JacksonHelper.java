package com.kwchina.wfm.interfaces.common;

import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;

public class JacksonHelper {

	public static String getJson(Object o) {
		StringWriter sw = new StringWriter();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(sw, o);
		}
		catch(Exception e) {
			
		}
		
		return sw.toString();
	}
}
