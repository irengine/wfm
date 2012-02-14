package com.kwchina.wfm.interfaces.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class QueryHelper {

	public final static String SORT_FIELD = "sidx";
	public final static String SORT_DIRECTION = "sord";
	public final static String IS_INCLUDE_CONDITION = "_search";
	public final static String FILTERS = "filters";
	
	public static enum FilterType {
		GROUP_OP,
		FIELD,
		OP,
		DATA
	};
	
	private static String addSingleQuoteToValue(String val)
	{
		String[] sv = val.split(",");
		String svString = "";
		for(int i=0;i<sv.length;i++){
			if (sv[i].length() > 0) {
				svString += "'" + sv[i] + "'";
				if (i != sv.length-1) {
					svString += ",";
				}
			}
		}
		
		return svString;
	}
	
	private static String getCondition(String field, String op, String data){
		
		if (!StringUtils.isEmpty(data)) {
			String result = "";
			if(op.trim().equals("eq")) {
				//等于
				result = field + " = '" + data +"' ";
			}else if(op.trim().equals("ne")) {
				//不等于
				result = field + " != '"+ data+"' ";
			}else if(op.trim().equals("lt"))	{
				//小于
				result = field + " < '"+ data+"' ";
			}else if(op.trim().equals("le")) {
				//小于等于
				result = field + " <= '"+ data+"' ";
			}else if(op.trim().equals("gt")) {
				//大于
				result = field + " > '"+ data+"' ";
			}else if(op.trim().equals("ge")) {
				//大于等于
				result = field + " >= '"+ data+"' ";
			}else if(op.trim().equals("bw")) {
				//开始于
				result = field + " like '"+ data+"%' ";
			}else if(op.trim().equals("bn")) {
				//不开始于
				result = field + " not like '"+ data+"%' ";
			}else if(op.trim().equals("in")) {
				//属于
				String svString = addSingleQuoteToValue(data);
				result = field + " in (" + svString + ") ";
			}else if(op.trim().equals("ni")) {
				//不属于
				String svString = addSingleQuoteToValue(data);
				result = field + " not in (" + svString + ") ";
			}else if(op.trim().equals("ew")) {
				//结束于
				result = field + " like '%"+ data+"' ";
			}else if(op.trim().equals("en")) {
				//不结束于
				result = field + " not like '%"+ data+"' ";
			}else if(op.trim().equals("cn")) {
				//包含
				result = field + " like '%"+ data+"%' ";
			}else if(op.trim().equals("nc")) {
				//不包含
				result = field + " not like '%"+ data+"%' ";
			}
			
			return result;
		}
		
		return "";
	}
	
	public static Map<String, String> getQueryParameters(HttpServletRequest request)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put(SORT_FIELD, request.getParameter(SORT_FIELD));
		parameters.put(SORT_DIRECTION, request.getParameter(SORT_DIRECTION));
		parameters.put(IS_INCLUDE_CONDITION, request.getParameter(IS_INCLUDE_CONDITION));
		parameters.put(FILTERS, request.getParameter(FILTERS));
		
		return parameters;
	}
	
	@SuppressWarnings("unchecked")
	public static String getWhereClause(String filters) {
		
		try {
			if (!StringUtils.isEmpty(filters)) {
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Map<String, Object>> maps = objectMapper.readValue(filters, Map.class);
				
				String groupOp = maps.get("groupOp").toString();
			
				ArrayList<Map<String, Object>> rules = (ArrayList<Map<String, Object>>)maps.get("rules");
				List<String> conditions = new ArrayList<String>();
				
				for(Map<String, Object> rule:rules) {
					String condition = getCondition(rule.get("field").toString(), rule.get("op").toString(), rule.get("data").toString());
					conditions.add(condition);
				}
				
				return StringUtils.join(conditions, groupOp);
			}
		}
		catch(Exception e) {
		}
		
		return "";
	}

}
