package com.kwchina.wfm.infrastructure.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesHelper {
	   private static Map<String, String> propertiesMap;
	   
	   private static void processProperties() {

	        try {
		        propertiesMap = new HashMap<String, String>();
		        Resource resource = new ClassPathResource("/application.properties");
				Properties props = PropertiesLoaderUtils.loadProperties(resource);
				for (String key : props.stringPropertyNames()) {
					propertiesMap.put(key, props.getProperty(key));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 
	    public static String getProperty(String name) {
	    	
	    	if (null == propertiesMap)
	    		processProperties();
	        return propertiesMap.get(name);
	    }
}
