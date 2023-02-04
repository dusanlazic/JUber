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

	@FindBy(id="ride-status-none")
	WebElement rideStatusNone;

	@FindBy(id = "no-driver-yet")
	WebElement noDriverYet;

	@FindBy(xpath = "//app-ride-details")
	WebElement header;

	@FindBy(xpath = "//*[contains(@class, 'toast-error')]")
	WebElement toastError;

	@FindBy(id="decline-ride-driver")
	WebElement declineRideDriver;

	@FindBy(id="accept-ride-driver")
	WebElement acceptRideDriver;

	@FindBy(id="passengers-got-in")
	WebElement passengersGotIn;

	@FindBy(id="abandon-ride-driver")
	WebElement abandonRideDriver;

	@FindBy(id="abandon-ride-driver-text")
	WebElement abandonRideDriverText;

	@FindBy(id="abandon-ride-driver-submit")
	WebElement abandonRideDriverSubmit;

	@FindBy(id="panic-button")
	WebElement panicButton;

	@FindBy(id="ride-finished-driver")
	WebElement rideFinishedDriver;

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

	public void waitRideStatusFinished() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.rideStatusFinished));
	}

	public void waitRideStatusNone() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.rideStatusNone));
	}

	public void declineRideDriver() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.declineRideDriver));
		declineRideDriver.click();
	}

	public void acceptRideDriver() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.acceptRideDriver));
		acceptRideDriver.click();
	}

	public void abandonRideDriver() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.abandonRideDriver));
		abandonRideDriver.click();

		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.abandonRideDriverText));
		abandonRideDriverText.sendKeys("Ubre...");

		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.abandonRideDriverSubmit));
		abandonRideDriverSubmit.click();
	}

	public void passengersGotIn() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.passengersGotIn));
		passengersGotIn.click();
	}

	public void rideFinishedDriver() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.rideFinishedDriver));
		rideFinishedDriver.click();
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

	public void waitPanicButton() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.panicButton));
	}

	public void panic() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.panicButton));
		panicButton.click();
	}
}
