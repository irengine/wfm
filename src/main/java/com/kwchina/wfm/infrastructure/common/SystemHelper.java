package com.kwchina.wfm.infrastructure.common;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.kwchina.wfm.interfaces.organization.dto.ErrorDTO;

@Aspect
public class SystemHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemHelper.class);

	@Pointcut("execution(* com.kwchina.wfm.interfaces.*.*(..))")
	public void inWebLayer() {}
	
	@Before("com.kwchina.wfm.infrastructure.common.SystemHelper.inWebLayer() && target(bean)")
	public void doActionRecord(JoinPoint jp, Object bean) {
		
		logger.info("Before: " + bean.toString() + jp.toLongString());
	}
	
	@AfterThrowing("com.kwchina.wfm.infrastructure.common.SystemHelper.inWebLayer() && args(locale, model,..)")
	public void doActionRecovery(Locale locale, Model model) {

		logger.info("Handling exception.");
	}
	
	@Pointcut("execution(* com.kwchina.wfm.interfaces.organization.web.SystemController.get*(..))")
	public void inWebControllerLayerGetMethod() {}
	
	@Around("com.kwchina.wfm.infrastructure.common.SystemHelper.inWebControllerLayerGetMethod() && args(request, response,..)")
	public Object doHandleExceptionWithGet(ProceedingJoinPoint pjp, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			Object retVal = pjp.proceed();
			return retVal;
		} catch (Throwable t) {
			logger.warn("Error:", t);

			ErrorDTO error = new ErrorDTO(t.getMessage());
			HttpHelper.output(response, error);
			return null;
		}
	}
	
	@Pointcut("execution(* com.kwchina.wfm.interfaces.organization.web.SystemController.save*(..))")
	public void inWebControllerLayerSetMethod() {}
	
	@Around("com.kwchina.wfm.infrastructure.common.SystemHelper.inWebControllerLayerSetMethod() && args(response,..)")
	public Object doHandleExceptionWithSet(ProceedingJoinPoint pjp, HttpServletResponse response) throws Throwable {
		try {
			Object retVal = pjp.proceed();
			HttpHelper.output(response, "1");
			return retVal;
		} catch (Throwable t) {
			logger.warn("Error:", t);

			HttpHelper.output(response, "0");
			return null;
		}
	}	
	
}
