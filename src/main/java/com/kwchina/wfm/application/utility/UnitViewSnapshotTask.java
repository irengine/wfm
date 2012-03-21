package com.kwchina.wfm.application.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitViewSnapshotTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(UnitViewSnapshotTask.class);
	
	private String name;
	
	public UnitViewSnapshotTask(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		logger.info("UnitSnapshotTask {} executed.", name);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}
}
