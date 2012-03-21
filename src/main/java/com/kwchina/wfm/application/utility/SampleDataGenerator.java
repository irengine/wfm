package com.kwchina.wfm.application.utility;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SampleDataGenerator implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("*** Loading data ***");
	    WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
	    DataSource dataSource = (DataSource) BeanFactoryUtils.beanOfType(context, DataSource.class);
	    PlatformTransactionManager transactionManager = (PlatformTransactionManager) BeanFactoryUtils.beanOfType(context, PlatformTransactionManager.class);
	    TransactionTemplate tt = new TransactionTemplate(transactionManager);
	    loadSampleData(new JdbcTemplate(dataSource), tt);
	}
	
	private static void executeUpdate(JdbcTemplate jdbcTemplate, String sql, Object[][] args) {
		for (Object[] arg : args) {
			jdbcTemplate.update(sql, arg);
		}
	}

	public static void loadSampleData(final JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				// to record system startup time.
				loadDummyObjects(jdbcTemplate);
			}
		});
	}
	
	private static void loadDummyObjects(final JdbcTemplate jdbcTemplate) {
		// dummy object
		String sql = "insert into dummyobject(uuid) values(?)";
//		UUID.randomUUID()
		Object[][] args = { {new Date().toString()} };

		executeUpdate(jdbcTemplate, sql, args);
	}

}
