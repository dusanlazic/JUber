package com.nwt.juber.e2e;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BasePage {

	public int index;

	public static String testName = "";

	public void log(WebDriver webDriver) {
		System.out.println("BasePage log");
		System.out.println(webDriver.getCurrentUrl());
		File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
		String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
		try {
			FileUtils.copyFile(scrFile, new File("./logs/" + testName + "/" + index + "-" + now + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
