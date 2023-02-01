package com.nwt.juber.e2e;

import com.nwt.juber.config.TestConfig;
import com.nwt.juber.e2e.pages.HomePage;
import com.nwt.juber.e2e.pages.LoginPage;
import com.nwt.juber.e2e.pages.RideDetailsPage;
import com.nwt.juber.model.Ride;
import com.nwt.juber.repository.RideRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles("test")
public class OrderTests extends TestBase {


	@Test
	@Disabled
	@Rollback
	public void Order_is_waiting_for_driver() {
		createDrivers(2);
		WebDriver window1 = windows.get(1);
		WebDriver window2 = windows.get(0);

		LoginPage loginPage1 = new LoginPage(window1);
		loginPage1.enterUsername("andrej.andrejevic@gmail.com");
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = new LoginPage(window2);
		loginPage2.enterUsername("zdravko.zdravkovic@gmail.com");
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		HomePage homePage1 = new HomePage(window1);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.selectHatchback();
		homePage1.orderRide();

		RideDetailsPage rideDetailsPage1 = new RideDetailsPage(window1);
	}

	@Test
	@Disabled
	@Rollback
	public void Order_fails_because_pal_is_busy() {
		createDrivers(2);
		WebDriver window1 = windows.get(1);
		WebDriver window2 = windows.get(0);


		LoginPage loginPage1 = new LoginPage(window1);
		loginPage1.enterUsername("andrej.andrejevic@gmail.com");
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = new LoginPage(window2);
		loginPage2.enterUsername("zdravko.zdravkovic@gmail.com");
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		HomePage homePage1 = new HomePage(window1);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.addPal("mile.miletic@gmail.com");
		homePage1.selectHatchback();
		homePage1.orderRide();
		String result = homePage1.waitToastError();
		Assertions.assertEquals("User is already in ride.", result);
	}

	@Test
	@Rollback
	public void Order_fails_because_no_driver_with_vehicle() {
		createDrivers(2);
		WebDriver window1 = windows.get(1);
		WebDriver window2 = windows.get(0);


		LoginPage loginPage1 = new LoginPage(window1);
		loginPage1.enterUsername("andrej.andrejevic@gmail.com");
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = new LoginPage(window2);
		loginPage2.enterUsername("zdravko.zdravkovic@gmail.com");
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		HomePage homePage1 = new HomePage(window1);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");

		homePage1.selectEstate();

		homePage1.orderRide();
		String result = homePage1.waitToastError();
		System.out.println(result);
		close = false;
	}


}
