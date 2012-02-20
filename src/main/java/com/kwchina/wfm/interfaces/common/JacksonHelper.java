package com.kwchina.wfm.interfaces.common;

import java.io.StringWriter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.organization.Unit;

public class JacksonHelper {
	
    @JsonIgnoreProperties({"parent", "children"})
    @JsonPropertyOrder(value={})
	private interface UnitFilter {
		
	}
    
    private interface EmployeeFilter {
    	
    }
    
    public static String getFilterJson(Object o) {
		StringWriter sw = new StringWriter();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.getSerializationConfig().addMixInAnnotations(Employee.class, EmployeeFilter.class);
			objectMapper.getSerializationConfig().addMixInAnnotations(Unit.class, UnitFilter.class);
			objectMapper.writeValue(sw, o);
		}
		catch(Exception e) {
			
		}
		
		return sw.toString();
    }

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
