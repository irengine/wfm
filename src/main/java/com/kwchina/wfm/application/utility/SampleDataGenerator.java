package com.kwchina.wfm.application.utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SampleDataGenerator implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("*** Loading data ***");
		loadData();
	}

	private void loadData() {
	}

}
