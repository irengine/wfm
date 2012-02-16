package com.kwchina.wfm.interfaces.common;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.junit.Test;

import com.kwchina.wfm.domain.model.organization.Employee;

public class JsonTests {

	@JsonFilter("myFilter")
	static class Bean {
		public String name;
		public int age;
	}

	@Test
	public void testFilter() throws JsonGenerationException, JsonMappingException,
			IOException {
		Bean value = new Bean();
		value.name = "XX";
		value.age = 10;
		ObjectMapper mapper = new ObjectMapper();
		// first, construct filter provider to exclude all properties but
		// 'name', bind it as 'myFilter'
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("myFilter",
						SimpleBeanPropertyFilter.filterOutAllExcept("name"));
		// and then serialize using that filter provider:
		String json = mapper.writer(filters).writeValueAsString(value);
		System.out.println(json);
	}
	
	@Test
	public void testSerializeDateField() throws JsonGenerationException, JsonMappingException, IOException
	{
		Employee e = new Employee();
		
		Calendar c = Calendar.getInstance();
		Date d = c.getTime();
		
		e.setCode("1");
		e.setName("Alex");
		e.setBirthday(d);
		
		StringWriter sw = new StringWriter();

		ObjectMapper objectMapper = new ObjectMapper();
		
		DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		objectMapper.getSerializationConfig().withDateFormat(myDateFormat); 
		objectMapper.getDeserializationConfig().withDateFormat(myDateFormat); 
		
		objectMapper.writeValue(sw, e);
		System.out.println(sw.toString());
	}
	
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
