package com.nwt.juber.e2e.pages;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nwt.juber.e2e.BasePage;
import com.nwt.juber.e2e.logging.LogSelenium;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@LogSelenium
public class AdminPage extends BasePage {

	@FindBy(xpath = "//*[contains(@class, 'is-user-info')]")
	WebElement userInfo;
	
	public	WebDriver webDriver;

	public AdminPage() {
		
	}

	public AdminPage(WebDriver webDriver, int index) {
		System.out.println("Creating admin page");
		this.index = index;
		this.webDriver = webDriver;
		PageFactory.initElements(webDriver, this);
	}

	public String getUserInfo() {
		new WebDriverWait(webDriver, Duration.of(5, ChronoUnit.SECONDS))
			.until(ExpectedConditions.visibilityOf(userInfo));
		return userInfo.getText();
	}
}
