package com.kwchina.wfm.interfaces.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class QueryHelperTests {
	
	@Test
	@SuppressWarnings({ "unchecked" })
	public void testJson2Map() throws JsonParseException, JsonMappingException, IOException
	{
		String json = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"gender\",\"op\":\"eq\",\"data\":\"1\"}, {\"field\":\"age\",\"op\":\"eq\",\"data\":\"20\"}]}";
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> maps = objectMapper.readValue(json, Map.class);
		
		assertEquals("AND", maps.get("groupOp"));
		
		ArrayList<Map<String, Object>> rules = (ArrayList<Map<String, Object>>)maps.get("rules");
		assertEquals(2, rules.size());
		
		for(Map<String, Object> rule:rules) {
			if (rule.get("field").equals("gender")) {
				assertEquals("1", rule.get("data"));
				assertEquals("eq", rule.get("op"));
			} else if (rule.get("field").equals("age")) {
				assertEquals("20", rule.get("data"));
				assertEquals("eq", rule.get("op"));
			} else {
				fail("unknown value");
			}
		}
	}

}
