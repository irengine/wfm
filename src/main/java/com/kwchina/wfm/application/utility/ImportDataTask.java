package com.kwchina.wfm.application.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.infrastructure.common.PropertiesHelper;

public class ImportDataTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ImportDataTask.class);
	
	private String name;
	private WebApplicationContext springContext;
	
	public ImportDataTask(String name, WebApplicationContext springContext) {
		this.name = name;
		this.springContext = springContext;
	}
	
	@Override
	public void run() {
		logger.info("ImportDataTask {} executed.", name);
		
		com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade employeeService = springContext.getBean(com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade.class);

        Connection conn = null;

        try {
			// register oracle driver
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (Exception e) {
				throw new SQLException("Oracle JDBC is not available", e);
			}

			// connect to oracle and login
			String url = PropertiesHelper.getProperty("IMPORT_DATABASE");
			conn = DriverManager.getConnection(url, PropertiesHelper.getProperty("IMPORT_USER"), PropertiesHelper.getProperty("IMPORT_PASSWORD"));

			// create the SQL statement
			String sql = "SELECT WORK_DATE, STAFF_ID, SHIFT_ID FROM WAG_TASK_INFO WHERE WORK_DATE = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);

			Date date = new Date();
			stmt.setString(1, DateHelper.getString(date));

			// fetch and display the results
			Map<String, String> orders = new HashMap<String, String>();
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("STAFF_ID") + " , "	+ rs.getString("SHIFT_ID"));
				orders.put(rs.getString("STAFF_ID"), rs.getString("SHIFT_ID"));
			}
			
			employeeService.importWorkOrder(date, orders);
			rs.close();
			stmt.close();
			conn.close();
		}
        catch(Exception e) {
        	logger.error(e.getMessage());
        	e.printStackTrace();
        }
        finally {
        	if (conn != null)
        		try {
        			conn.close();
        		}
        		catch(Exception e) {};
        	
        }
		
	}
}
