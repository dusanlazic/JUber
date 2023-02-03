package com.nwt.juber.e2e.pages;

import com.nwt.juber.e2e.BasePage;
import com.nwt.juber.e2e.logging.LogSelenium;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@LogSelenium
public class HomePage extends BasePage {

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

	@FindBy(xpath = "//label[contains(@for, 'car-estate')]")
	WebElement carEstate;

	@FindBy(xpath = "//label[contains(@for, 'car-limousine')]")
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

	@FindBy(xpath = "//*[contains(@class, 'toast-error')]")
	WebElement toastError;

	@FindBy(id = "notification-bell-icon")
	WebElement notificationBellIcon;


	@FindBy(xpath = "//app-ride-invite")
	List<WebElement> rideInvite;

	@FindBy(xpath = "//app-notification-item")
	List<WebElement> notificationItems;

	public WebDriver webDriver;

	public HomePage() {

	}

	public HomePage(WebDriver webDriver, int index) {
		System.out.println("Creating home page");
		this.index = index;
		this.webDriver = webDriver;
		webDriver.navigate().to("http://localhost:3000/home");
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
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(), '"+address+"')]")));
	}

	public void selectHatchback() {
		(new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Baby friendly')]")));

		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.carHatchback));
		new Actions(webDriver)
				.moveToElement(this.carHatchback, 5, 5).click().perform();
	}

	public void selectLimousine() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.carLimousine));
		new Actions(webDriver)
				.moveToElement(this.carLimousine)
				.moveByOffset(10, 10)
				.click().perform();
	}

	public void selectEstate() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.carEstate));
		new Actions(webDriver)
				.moveToElement(this.carEstate)
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
				.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("calculated-distance"), "0 km")));

		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("calculated-time"), "0 min")));

		System.out.println(webDriver.findElement(By.id("calculated-distance")).getText());
		System.out.println(webDriver.findElement(By.id("calculated-time")).getText());

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

	public String waitToastError() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.toastError));
		return this.toastError.getText();
	}

	public String optionalWaitForToastError() {
		try {
			(new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS)))
					.until(ExpectedConditions.visibilityOf(this.toastError));
			return this.toastError.getText();
		} catch (Exception e) {
			return null;
		}
	}

	public void openNotifications() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.notificationBellIcon));
		this.notificationBellIcon.click();
		this.log(webDriver);

	}

	public void acceptFirstRideInvitation() {
		this.openNotifications();
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOfAllElements(this.rideInvite));
		this.rideInvite.get(0).findElement(By.xpath("//button[contains(text(), 'Accept')]")).click();
	}

	public void declineFirstRideInvitation() {
		this.openNotifications();
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOfAllElements(this.rideInvite));
		this.rideInvite.get(0).findElement(By.xpath("//button[contains(text(), 'Decline')]")).click();
	}

	public void setScheduledTime(String hours, String minutes) {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.visibilityOf(this.scheduleHours));
		this.scheduleHours.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, hours);
		this.scheduleMinutes.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, minutes);
	}



}
