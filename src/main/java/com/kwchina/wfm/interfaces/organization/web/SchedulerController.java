package com.kwchina.wfm.interfaces.organization.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.infrastructure.common.PropertiesHelper;
import com.kwchina.wfm.interfaces.organization.facade.EmployeeServiceFacade;

@Controller
public class SchedulerController {
	
	@Autowired
	EmployeeServiceFacade employeeService;
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerController.class);
	
	@RequestMapping(value = "/taskImportData", method = RequestMethod.GET)
	public void importData(HttpServletRequest request) {
		logger.info("ImportDataTask executed.");
		
		String executeDate = request.getParameter("executeDate");
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

			Date date = DateHelper.addDay(DateHelper.getDate(executeDate), -1);
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
	
	@RequestMapping(value = "/taskGenerateMonthTimeSheet", method = RequestMethod.GET)
	public void generateMonthTimeSheet(HttpServletRequest request) {
		logger.info("GenerateMonthTimeSheetTask executed.");
		
		String executeDate = request.getParameter("executeDate");
		if (StringUtils.isEmpty(executeDate))
			executeDate = DateHelper.getString(DateHelper.getFinancialMonth());
		
        try {
			employeeService.generateMonthTimeSheet(executeDate);
		}
        catch(Exception e) {
        	logger.error(e.getMessage());
        }
        finally {
        }		
	}
	
	@RequestMapping(value = "/taskGenerateDayTimeSheet", method = RequestMethod.GET)
	public void generateDayTimeSheet(HttpServletRequest request) {
		logger.info("GenerateDayTimeSheetTask executed.");
		
		String executeDate = request.getParameter("executeDate");
		if (StringUtils.isEmpty(executeDate))
			executeDate = DateHelper.getString(new Date());
		
        try {
			employeeService.generateDayTimeSheet(executeDate);
		}
        catch(Exception e) {
        	logger.error(e.getMessage());
        }
        finally {
        }
	}
	
	@RequestMapping(value = "/taskVacationSnapshot", method = RequestMethod.GET)
	public void vacationSnapshot(HttpServletRequest request) {
		logger.info("VacationSnapshotTask executed.");
		
		String executeDate = request.getParameter("executeDate");
		if (StringUtils.isEmpty(executeDate))
			executeDate = DateHelper.getString(DateHelper.getFinancialMonth());
		
		try {
			employeeService.calculateVacation(DateHelper.getDate(executeDate));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/taskOvertimeSnapshot", method = RequestMethod.GET)
	public void overtimeSnapshot(HttpServletRequest request) {
		logger.info("OvertimeSnapshotTask executed.");
		
		String executeDate = request.getParameter("executeDate");
		if (StringUtils.isEmpty(executeDate))
			executeDate = DateHelper.getString(DateHelper.getFinancialMonth());
		
		try {
			employeeService.calculateOvertime(DateHelper.getDate(executeDate));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
