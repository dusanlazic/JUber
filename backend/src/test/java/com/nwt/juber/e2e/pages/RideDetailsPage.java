package com.nwt.juber.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

	@FindBy(xpath = "//*[contains(@class, 'toast-success')]")
	WebElement toastSuccess;

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

	@FindBy(id="review-toggle-modal")
	WebElement reviewToggleModal;

	@FindBy(id="review-input-comment")
	WebElement reviewInputComment;

	@FindBy(id="review-input-driver-rating")
	WebElement reviewInputDriverRating;

	@FindBy(id="review-input-vehicle-rating")
	WebElement reviewInputVehicleRating;

	@FindBy(id="review-submit")
	WebElement reviewSubmit;

	@FindBy(id = "profile-button-ride-details")
	WebElement profileButton;

	@FindBy(xpath = "//p[@id='price-per-passenger']//b")
	WebElement pricePerPassenger;

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

	public String waitToastSuccess() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.toastSuccess));
		return this.toastSuccess.getText();
	}

	public void waitToastDisappear() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(this.toastSuccess)));
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(this.toastError)));
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

	public void leaveReview() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.reviewToggleModal));
		reviewToggleModal.click();

		String comment = "Ubre kako dobra vožnja";
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.reviewInputComment));
		reviewInputComment.sendKeys(comment);

		new Actions(webDriver)
				.moveToElement(reviewInputDriverRating).moveByOffset(30, 3)
				.click()
				.moveToElement(reviewInputVehicleRating).moveByOffset(50, 3)
				.click()
				.perform();

		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.reviewSubmit));
		reviewSubmit.click();

		(new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//*[contains(text(), '%s')]", comment))));
	}

	public void goToProfile() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.toastSuccess));
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.invisibilityOf(this.toastSuccess));
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.profileButton));

		profileButton.click();
	}

	public String getPricePerPassenger() {
		new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS))
				.until(ExpectedConditions.visibilityOf(pricePerPassenger));
		return pricePerPassenger.getText();
	}
}
