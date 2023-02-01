package com.nwt.juber.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class HomePage {

	@FindBy(xpath = "//button[contains(text(), 'Add')]")
	WebElement addPlaceButton;

	@FindBy(xpath = "//button[contains(@class, 'save-btn')]")
	WebElement savePlaceButton;

	@FindBy(xpath = "//button[contains(@class, 'cancel-btn')]")
	WebElement cancelPlaceButton;

	@FindBy(xpath = "//input[contains(@placeholder, 'Enter')]")
	WebElement addressInput;

	@FindBy(xpath = "//label[contains(@for, 'car-hatchback')]")
	WebElement carHatchback;

	@FindBy(id = "car-estate")
	WebElement carEstate;

	@FindBy(id = "car-limousine")
	WebElement carLimousine;

	@FindBy(id = "baby-friendly")
	WebElement babyFriendly;

	@FindBy(id = "pet-friendly")
	WebElement petFriendly;

	@FindBy(id = "schedule-hours")
	WebElement scheduleHours;

	@FindBy(id = "schedule-minutes")
	WebElement scheduleMinutes;

	@FindBy(id = "continue-to-payment-btn")
	WebElement continueToPaymentButton;

	@FindBy(id = "add-pals-btn")
	WebElement addPalsButton;

	@FindBy(id = "add-pal-email-input")
	WebElement addPalEmailInput;

	@FindBy(id = "add-pal-confirm-button")
	WebElement addPalConfirmButton;


	WebDriver webDriver;

	public HomePage(WebDriver webDriver) {
		webDriver.navigate().to("http://localhost:3000/home");
		this.webDriver = webDriver;
		PageFactory.initElements(webDriver, this);
	}

	public void addPlace(String address) {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.addPlaceButton));
		this.addPlaceButton.click();
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.addressInput));
		this.addressInput.sendKeys(address);
		new WebDriverWait(webDriver, Duration.of(3, ChronoUnit.SECONDS));
		this.savePlaceButton.click();
	}

	public void selectHatchback() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.carHatchback));
		new Actions(webDriver)
				.moveToElement(this.carHatchback)
				.moveByOffset(10, 10)
				.click().perform();
	}

	public void setHours(String hours) {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.scheduleHours));
		this.scheduleHours.sendKeys(hours);
	}

	public void setMinutes(String minutes) {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.scheduleMinutes));
		this.scheduleMinutes.sendKeys(minutes);
	}

	public void orderRide() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.continueToPaymentButton));
		this.continueToPaymentButton.click();
	}

	public void addPal(String email) {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.addPalsButton));
		this.addPalsButton.click();
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.addPalEmailInput));
		this.addPalEmailInput.sendKeys(email);
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.addPalEmailInput));
		this.addPalConfirmButton.click();
	}


}
