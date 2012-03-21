package com.kwchina.wfm.application.utility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Scheduler implements ServletContextListener  {
	
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newScheduledThreadPool(2);
        scheduler.scheduleAtFixedRate(new UnitViewSnapshotTask("U"), 0, 1, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new EmployeeViewSnapshotTask("E"), 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
