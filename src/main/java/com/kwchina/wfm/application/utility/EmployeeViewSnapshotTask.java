package com.kwchina.wfm.application.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeViewSnapshotTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeViewSnapshotTask.class);
	
	private String name;
	
	public EmployeeViewSnapshotTask(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		logger.info("EmployeeSnapshotTask {} executed.", name);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}
}
