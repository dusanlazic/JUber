package com.nwt.juber.e2e.pages;

import com.nwt.juber.e2e.BasePage;
import com.nwt.juber.e2e.logging.LogSelenium;
import org.openqa.selenium.By;
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
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@LogSelenium
public class PassengerPage extends BasePage {

	@FindBy(xpath = "//*[contains(@class, 'is-user-info')]")
	WebElement userInfo;

	@FindBy(xpath = "//*[contains(text(), 'Past rides') and contains(@class, 'navigation-item')]")
	WebElement pastRidesLink;

	@FindBy(xpath = "//*[contains(text(), 'Balance') and contains(@class, 'navigation-item')]")
	WebElement balanceLink;

	@FindBy(xpath = "app-past-rides/div/app-past-rides-table/table/tbody/td")
	List<WebElement> rides;

	public WebDriver webDriver;

	public PassengerPage() {

	}

	public PassengerPage(WebDriver webDriver, int index) {
		System.out.println("Creating passenger page");
		this.index = index;
		this.webDriver = webDriver;
		PageFactory.initElements(webDriver, this);
	}

	public String getUserInfo() {
		new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS))
			.until(ExpectedConditions.visibilityOf(userInfo));
		return userInfo.getText();
	}

	public void navigateToPastRides() {
		new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS))
				.until(ExpectedConditions.visibilityOf(pastRidesLink));
		pastRidesLink.click();
	}

	public void navigateToBalance() {
		new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS))
				.until(ExpectedConditions.visibilityOf(balanceLink));
		balanceLink.click();
	}

	public void waitForTextToAppear(String text) {
		(new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS)))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//*[contains(text(), '%s')]", text))));
	}
}
