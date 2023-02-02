package com.nwt.juber.e2e.pages;

import com.nwt.juber.e2e.BasePage;
import com.nwt.juber.e2e.logging.LogSelenium;
import com.nwt.juber.e2e.logging.SkipLog;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginPage extends BasePage {


	@FindBy(xpath = "//input[@type='email']")
	WebElement usernameInput;

	@FindBy(xpath = "//input[@type='password']")
	WebElement passwordInput;

	@FindBy(xpath = "//button[@type='submit']")
	WebElement loginButton;

	public WebDriver webDriver;

	public void init(WebDriver webDriver) {
		this.webDriver = webDriver;
		this.webDriver.navigate().to("http://localhost:3000/login");
		PageFactory.initElements(webDriver, this);
	}

	public LoginPage() {
	}

	public LoginPage(WebDriver webDriver, int index) {
		this.index = index;
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
	@SkipLog
	public void login() {
		(new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.elementToBeClickable(this.loginButton));
		this.loginButton.click();
	}
}
