package com.kwchina.wfm.interfaces.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.junit.Before;
import org.junit.Test;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.Employee.Gender;
import com.kwchina.wfm.domain.model.employee.EmployeeId;
import com.kwchina.wfm.domain.model.organization.Unit;

public class JacksonHelperTest {
	
	private Unit unit;
	private Employee employee;
	private ObjectMapper mapper;
	
	@Before
	public void setUp() throws Exception {
		unit = new Unit("X");
		Unit child = new Unit("XX");
		unit.addChild(child);
		
		Date date = DateUtils.parseDate("2012-02-14",new String[]{"yyyy-MM-dd"});
		employee = new Employee(new EmployeeId("0001"), "Alex Tang", Gender.MALE, date, date, date);
	}
	
    @JsonIgnoreProperties({"parent", "children"})
    @JsonPropertyOrder(value={})
	private interface UnitFilter {
		
	}
    
    private interface EmployeeFilter {
    	
    }

	@Test
	@SuppressWarnings("deprecation")
	public void testSerializeAssociatedObjects() throws IOException {
		DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd"); 

		mapper = new ObjectMapper();
        mapper.getSerializationConfig().addMixInAnnotations(Unit.class, UnitFilter.class);
        System.out.println(mapper.writeValueAsString(unit));

        mapper = new ObjectMapper();
        mapper.getSerializationConfig().addMixInAnnotations(Employee.class, EmployeeFilter.class);
        mapper.getSerializationConfig().addMixInAnnotations(Unit.class, UnitFilter.class);
        mapper.getSerializationConfig().setDateFormat(DATEFORMAT); 
        System.out.println(mapper.writeValueAsString(employee));

	}
	
	@JsonFilter("myFilter")
	static class Bean {
		public String name;
		public int age;
	}

	@Test
	public void testSimpleBeanPropertyFilter() throws JsonGenerationException, JsonMappingException,
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
	public void testMapSort() throws JsonGenerationException, JsonMappingException, IOException
	{
		TreeMap<String,String> maps = new TreeMap<String, String>();
		maps.put("1", "1");
		maps.put("5", "5");
		maps.put("3", "3");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(maps);
		System.out.println(json);
	
		
		
	}
}
