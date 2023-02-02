package com.nwt.juber.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

	@FindBy(xpath = "//*[contains(@class, 'toast-error')]")
	WebElement toastError;

	@FindBy(id="decline-ride-driver")
	WebElement declineRideDriver;

	@FindBy(xpath = "//*[contains(@class, 'person-full-name')]")
	List<WebElement> passengers;

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
			new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS))
					.until(ExpectedConditions.visibilityOf(header));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String waitToastError() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.toastError));
		return this.toastError.getText();
	}

	public void waitRideStatusFailed() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.rideStatusFailed));
	}

	public void declineRideDriver() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.declineRideDriver));
		declineRideDriver.click();
	}

	public void waitForPassengerToAppear(String fullName) {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[contains(@class, 'person-full-name')]"), 0));
		for (WebElement passenger : passengers) {
			System.out.println(passenger.getText());
			if (passenger.getText().equals(fullName)) {
				return;
			}
		}
		assert false;
	}

}
