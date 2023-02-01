package com.nwt.juber.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class RideDetailsPage {

	@FindBy(id = "driver-full-name")
	WebElement driverFullName;

	@FindBy(id = "ride-status-failed")
	WebElement rideStatusFailed;

	@FindBy(id = "ride-status-finished")
	WebElement rideStatusFinished;

	@FindBy(id = "no-driver-yet")
	WebElement noDriverYet;

	@FindBy(xpath = "//app-ride-details")
	WebElement header;

	WebDriver webDriver;
	public RideDetailsPage(WebDriver webDriver) {
		this.webDriver = webDriver;
		PageFactory.initElements(webDriver, this);
	}

	public String getDriverFullName() {
		new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS))
				.until(ExpectedConditions.visibilityOf(driverFullName));
		return driverFullName.getText();
	}

	public void waitNoDriverYet() {
		new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS))
				.until(ExpectedConditions.visibilityOf(noDriverYet));
	}

	public boolean optionalNoDriverYet() {
		try {
			new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS))
					.until(ExpectedConditions.visibilityOf(noDriverYet));
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	public boolean optionalHeader() {
		try {
			new WebDriverWait(webDriver, Duration.of(3, ChronoUnit.SECONDS))
					.until(ExpectedConditions.visibilityOf(header));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
