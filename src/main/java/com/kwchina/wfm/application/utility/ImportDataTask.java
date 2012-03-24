package com.kwchina.wfm.application.utility;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportDataTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ImportDataTask.class);
	
	private String name;
	
	public ImportDataTask(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		logger.info("ImportDataTask {} executed.", name);

		
		 // set up example start and end days
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, -7);
        Calendar endDate = Calendar.getInstance();
        
        Connection conn = null;

        try {
	        // register oracle driver
	        try {
	            Class.forName("oracle.jdbc.driver.OracleDriver");
	        } catch ( Exception e ) {
	            throw new SQLException("Oracle JDBC is not available",e);
	        }
	
	        // connect to oracle and login
	        String url = "jdbc:oracle:thin:@//localhost:1521/orcl";
	        conn = DriverManager.getConnection(url,"scott","tiger");
	
	        // create the SQL statement
	        String sql = "SELECT EMPNO,ENAME FROM EMP WHERE HIREDATE BETWEEN ? AND ?";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	
	        // set the start and end dates into the SQL
	        Date date = new Date(startDate.getTimeInMillis());
	        stmt.setDate(1, date);
	
	        date = new Date(endDate.getTimeInMillis());
	        stmt.setDate(2, date);
	
	        // fetch and display the results
	        ResultSet rs = stmt.executeQuery();
	        while( rs.next() ) {
	            System.out.println(rs.getInt("EMPNO")+" , "+rs.getString("ENAME"));
	        }
	        rs.close();
	        stmt.close();
	        conn.close();
	        }
	    catch(Exception e) {
	        	
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
