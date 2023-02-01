package com.nwt.juber.e2e.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	public LoggingAspect() {
		System.out.println("LoggingAspect created");
	}

	@After("execution(* com.nwt.juber.e2e.pages.*.*(..))")
	public void logMethodExecution(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
		System.out.println("Method is executed!");
		System.out.println(joinPoint.getSignature().getName());
		Object target = joinPoint.getTarget();
		System.out.println(target.getClass().getName());
//		WebDriver webDriver = (WebDriver) target.getClass().getDeclaredField("webDriver").get(target);
//		System.out.println("WebDriver URL: " + webDriver.getCurrentUrl());
	}

}
