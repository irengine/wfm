package com.kwchina.wfm.application.utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.support.GenericApplicationContext;
//
//import com.kwchina.wfm.domain.model.organization.Unit;
//import com.kwchina.wfm.domain.model.organization.UnitRepository;

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
//        GenericApplicationContext context = 
//                new AnnotationConfigApplicationContext( 
//                        "com.kwchina.wfm.infrastructure.persistence");
//        
//        UnitRepository unitRepository = context.getBean(UnitRepository.class);
//        
//		Unit root = unitRepository.getRoot("S");
//		
//		Unit l1 = new Unit("L1");
//		unitRepository.addChild(root, l1);
//		
//		Unit l11 = new Unit("L11");
//		unitRepository.addChild(l1, l11);
//
//		Unit l12 = new Unit("L12");
//		unitRepository.addChild(l1, l12);
//
//		Unit l2 = new Unit("L2");
//		unitRepository.addChild(root, l2);
//		
//		Unit l21 = new Unit("L21");
//		unitRepository.addChild(l2, l21);
//
//		Unit l22 = new Unit("L22");
//		unitRepository.addChild(l2, l22);
//
//		Unit l23 = new Unit("L23");
//		unitRepository.addChild(l2, l23);
	}

}
