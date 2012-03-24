package com.kwchina.wfm.application.utility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.wfm.infrastructure.common.DateHelper;

public class Scheduler implements ServletContextListener  {
	
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        final WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
    	
        scheduler = Executors.newScheduledThreadPool(2);
        scheduler.scheduleAtFixedRate(new VacationSnapshotTask("Vacation", springContext), DateHelper.getNextRuntimeMinutes(23), DateHelper.MINUTES_ONE_DAY, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new VacationSnapshotTask("Vacation", springContext), DateHelper.getNextRuntimeMinutes(1), DateHelper.MINUTES_ONE_DAY, TimeUnit.MINUTES);
//        scheduler.scheduleAtFixedRate(new UnitViewSnapshotTask("U"), 0, 1, TimeUnit.MINUTES);
//        scheduler.scheduleAtFixedRate(new EmployeeViewSnapshotTask("E"), 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
