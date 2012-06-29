package com.kwchina.wfm.interfaces.common;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.shift.AttendanceType;

public class JacksonHelper {
	
    @JsonIgnoreProperties({"parent", "children", "users", "leftId", "rightId"})
    @JsonPropertyOrder(value={})
	private interface CommonUnitFilter {
		
	}
    
    @JsonIgnoreProperties({"preferences"})
    @JsonPropertyOrder(value={})
	private interface CommonUserFilter {
		
	}
    
    @JsonIgnoreProperties({"vacations"})
    @JsonPropertyOrder(value={})
    private interface CommonEmployeeFilter {
    	
    }
    
    @JsonIgnoreProperties({"parent", "children", "users", "left", "right", "shiftType", "preferences"})
    @JsonPropertyOrder(value={})
	private interface TUnitFilter {
		
	}
    
    @JsonIgnoreProperties({"preferences"})
    @JsonPropertyOrder(value={})
	private interface TUserFilter {
		
	}
    
    @JsonIgnoreProperties({"vacations", "gender", "birthday", "beginDateOfWork", "beginDateOfJob", "shiftType", "preferences", "enable"})
    @JsonPropertyOrder(value={})
    private interface TEmployeeFilter {
    	
    }
    
    @JsonIgnoreProperties({"preferences", "enable"})
    @JsonPropertyOrder(value={})
	private interface TAttendanceTypeFilter {
		
	}
    
    @JsonIgnoreProperties({"unit", "employee"})
    @JsonPropertyOrder(value={})
	private interface TTimeSheetFilter {
		
	}


    public static String getUnitJsonWithFilters(Object o) {
    	
    	Map<Class<?>, Class<?>> filters = new HashMap<Class<?>, Class<?>>();
    	filters.put(Unit.class, CommonUnitFilter.class);
    	
    	return getJsonWithFilters(o, filters);
    }
    
    public static String getUserJsonWithFilters(Object o) {
    	
    	Map<Class<?>, Class<?>> filters = new HashMap<Class<?>, Class<?>>();
    	filters.put(Unit.class, CommonUnitFilter.class);
    	filters.put(User.class, CommonUserFilter.class);
    	
    	return getJsonWithFilters(o, filters);
    }
    
    public static String getEmployeeJsonWithFilters(Object o) {
    	
    	Map<Class<?>, Class<?>> filters = new HashMap<Class<?>, Class<?>>();
    	filters.put(Unit.class, CommonUnitFilter.class);
    	filters.put(Employee.class, CommonEmployeeFilter.class);
    	
    	return getJsonWithFilters(o, filters);
    }
    
    public static String getTimeSheetJsonWithFilters(Object o) {
    	
    	Map<Class<?>, Class<?>> filters = new HashMap<Class<?>, Class<?>>();
    	filters.put(Unit.class, TUnitFilter.class);
    	filters.put(User.class, TUserFilter.class);
    	filters.put(Employee.class, TEmployeeFilter.class);
    	filters.put(TimeSheet.class, TTimeSheetFilter.class);
    	filters.put(AttendanceType.class, TAttendanceTypeFilter.class);
    	
    	return getJsonWithFilters(o, filters);
    }

	public static String getJson(Object o) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(o);
		}
		catch(Exception e) {
			
		}
		
		return "";
	}
	
    public static String getJsonWithFilters(Object o, Map<Class<?>, Class<?>> filters) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			for(Map.Entry<Class<?>, Class<?>> filter : filters.entrySet()) {
				objectMapper.getSerializationConfig().addMixInAnnotations(filter.getKey(), filter.getValue());
			}
			return objectMapper.writeValueAsString(o);
		}
		catch(Exception e) {
			
		}
		
		return "";
    }
}
