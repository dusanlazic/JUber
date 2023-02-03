package com.nwt.juber.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.nwt.juber.config.TestConfig;
import com.nwt.juber.e2e.pages.AdminPage;
import com.nwt.juber.e2e.pages.HomePage;
import com.nwt.juber.e2e.pages.LoginPage;
import com.nwt.juber.e2e.pages.RideDetailsPage;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = { TestConfig.class })
@EnableAspectJAutoProxy
@ActiveProfiles("test")
public class LoginTests extends TestBase {
	

	@Autowired
	ApplicationContext applicationContext;

	@Test
	public void passenger_login_successful_valid_credentials() {
		BasePage.testName = "passenger_login_successful_valid_credentials";
		List<String> users = List.of("andrej.andrejevic@gmail.com");
		createDrivers(1, users);
		WebDriver window1 = windows.get(0);
		
		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();
		

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.selectHatchback();
	}

	@Test
	public void driver_login_successful_valid_credentials() {
		BasePage.testName = "driver_login_successful_valid_credentials";
		List<String> users = List.of("zdravko.zdravkovic@gmail.com");
		createDrivers(1, users);
		WebDriver window1 = windows.get(0);
		
		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();
		
		RideDetailsPage homePage1 = new RideDetailsPage(window1);
		assertTrue(homePage1.optionalHeader());
	}
	
	@Test
	public void admin_login_successful_valid_credentials() {
		BasePage.testName = "admin_login_successful_valid_credentials";
		String adminMail = "admin@juber.com";
		List<String> users = List.of(adminMail);
		createDrivers(1, users);
		WebDriver window1 = windows.get(0);
		
		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();
		
		AdminPage homePage1 = applicationContext.getBean(AdminPage.class, window1, 0);
		assertTrue(homePage1.getUserInfo().contains(adminMail));	
	}
	
	@Test
	public void invalid_login_email_not_verified() {
		BasePage.testName = "invalid_login_email_not_verified";
		List<String> users = List.of("neverifikovan@gmail.com");
		createDrivers(1, users);
		WebDriver window1 = windows.get(0);
		
		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();
		
		String result = loginPage1.waitToastError();
		assertTrue(result.contains("User account is locked"));
	}
	
	@Test
	public void invalid_login_wrong_password() {
		BasePage.testName = "invalid_login_wrong_password";
		List<String> users = List.of("andrej.andrejevic@gmail.com");
		createDrivers(1, users);
		WebDriver window1 = windows.get(0);
		
		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("badPassword");
		loginPage1.login();
		
		String result = loginPage1.waitToastError();
		assertTrue(result.contains("Bad credentials"));
	}
	
	@Test
	public void invalid_login_non_existing_user() {
		BasePage.testName = "invalid_login_non_existing_user";
		List<String> users = List.of("andrej.andrejevic@gmail.com");
		createDrivers(1, users);
		WebDriver window1 = windows.get(0);
		
		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername("niko@gmail.com");
		loginPage1.enterPassword("badPassword");
		loginPage1.login();
		
		String result = loginPage1.waitToastError();
		assertTrue(result.contains("User not found."));
	}
 }
