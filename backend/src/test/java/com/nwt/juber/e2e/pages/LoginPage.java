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

public class LoginPage {

	WebDriver webDriver;

	@FindBy(xpath = "//input[@type='email']")
	WebElement usernameInput;

	@FindBy(xpath = "//input[@type='password']")
	WebElement passwordInput;

	@FindBy(xpath = "//button[@type='submit']")
	WebElement loginButton;

	public LoginPage(WebDriver webDriver) {
		this.webDriver = webDriver;
		this.webDriver.navigate().to("http://localhost:3000/login");
		PageFactory.initElements(webDriver, this);
	}


	public void enterUsername(String username) {
		this.usernameInput.sendKeys(username);
	}

	public void enterPassword(String password) {
		this.passwordInput.sendKeys(password);
	}

	public void login() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.loginButton));
		this.loginButton.click();
	}
}
