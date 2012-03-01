package com.kwchina.wfm.infrastructure.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class SystemHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemHelper.class);

	@Pointcut("execution(* com.kwchina.wfm.interfaces.*.*(..))")
	public void inWebLayer() {}
	
	@Before("com.kwchina.wfm.infrastructure.common.SystemHelper.inWebLayer() && target(bean)")
	public void doActionRecord(JoinPoint jp, Object bean) {
		
		logger.info("AOP from here == " + bean.toString() + jp.toLongString());
	}
}
