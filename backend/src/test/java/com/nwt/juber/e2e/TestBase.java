package com.nwt.juber.e2e;

import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestBase {

	boolean close = true;

	List<WebDriver> windows = new ArrayList<>();

	public WebDriver openDriver(int number) {
		WebDriver driver;
		System.setProperty("webdriver.chrome.driver", "chromedriver");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		options.addArguments("--user-data-dir=/tmp/tmp" + number);
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		return driver;
	}

	public void createDrivers(int number) {
		for (int i = 0; i < number; i++) {
			WebDriver driver = openDriver(i);
			windows.add(driver);
		}
	}
	@AfterEach
	public void close() {
		if (!close) return;
		for (WebDriver window : windows) {
			window.close();
		}
	}

	public void sleep(int millies) {
		try {
			Thread.sleep(millies);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
