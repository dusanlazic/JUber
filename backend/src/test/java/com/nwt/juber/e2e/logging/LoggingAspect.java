package com.nwt.juber.e2e.logging;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.nwt.juber.e2e.BasePage;
import org.apache.commons.io.FileUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {
	public LoggingAspect() {
		System.out.println("LoggingAspect created");
	}


	public List<String> skipMethods = List.of("init", "login");

	@Pointcut("within(@com.nwt.juber.e2e.logging.LogSelenium *)")
	public void logSeleniumClasses() {}

	@Pointcut("@annotation(com.nwt.juber.e2e.logging.SkipLog)")
	public void skipMethod() {}

	@Pointcut("execution(* *(..))")
	public void anyMethod() {}

	@After("logSeleniumClasses() && anyMethod() && !skipMethod()")
	public void logMethodExecution(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
		for (String skipMethod: skipMethods) {
			if (joinPoint.getSignature().getName().startsWith(skipMethod)) {
				return;
			}
		}
		Object target = joinPoint.getTarget();
		WebDriver webDriver = (WebDriver) target.getClass().getDeclaredField("webDriver").get(target);
		if (webDriver == null) return;
		int index = (int) target.getClass().getSuperclass().getDeclaredField("index").get(target);
		String testName = BasePage.testName;
		log(webDriver, index, testName);
	}

	public void log(WebDriver webDriver, int index, String testName) {
		File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
		String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
		try {
			FileUtils.copyFile(scrFile, new File("./logs/" + testName + "/" + index + "-" + now + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
