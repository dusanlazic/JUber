package com.nwt.juber.e2e;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.nwt.juber.config.TestConfig;
import com.nwt.juber.dto.notification.RideInvitation;
import com.nwt.juber.e2e.pages.HomePage;
import com.nwt.juber.e2e.pages.LoginPage;
import com.nwt.juber.e2e.pages.RideDetailsPage;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.repository.PassengerRepository;
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
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class OrderTests extends TestBase {


	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	ApplicationContext applicationContext;

	@Test
	@Rollback
	public void Order_is_waiting_for_driver() {
		BasePage.testName = "Order_is_waiting_for_driver";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com");
		createDrivers(2, users);
		WebDriver window1 = windows.get(1);
		WebDriver window2 = windows.get(0);

		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = applicationContext.getBean(LoginPage.class, window2, 1);
		loginPage2.enterUsername(users.get(1));
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		HomePage homePage1 = new HomePage(window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.selectHatchback();
		homePage1.orderRide();

		RideDetailsPage rideDetailsPage1 = new RideDetailsPage(window1);
	}

	@Test
	@Rollback
	public void Order_fails_because_pal_is_busy() {
		BasePage.testName = "Order_fails_because_pal_is_busy";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com");

		createDrivers(2, users);
		WebDriver window1 = windows.get(1);
		WebDriver window2 = windows.get(0);


		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = applicationContext.getBean(LoginPage.class, window2, 1);
		loginPage2.enterUsername(users.get(1));
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.addPal("mile.miletic@gmail.com");
		homePage1.selectHatchback();
		homePage1.orderRide();
		String result = homePage1.waitToastError();
		assertEquals("User is already in ride.", result);
	}

	@Test
	@Rollback
	public void Order_fails_because_no_driver_with_vehicle() {
		BasePage.testName = "Order_fails_because_no_driver_with_vehicle";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com");

		createDrivers(2, users);
		WebDriver window1 = windows.get(1);
		WebDriver window2 = windows.get(0);


		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = applicationContext.getBean(LoginPage.class, window2, 1);
		loginPage2.enterUsername(users.get(1));
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");

		homePage1.selectEstate();

		homePage1.orderRide();
		String result = homePage1.waitToastError();
		System.out.println(result);
	}

	@Test
	@Rollback
	public void Order_at_the_same_time_one_fails() {
		BasePage.testName = "Order_at_the_same_time_one_fails";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "branimir.branimirovic@gmail.com", "zdravko.zdravkovic@gmail.com");
		createDrivers(3, users);
		WebDriver window1 = windows.get(0);
		WebDriver window2 = windows.get(1);
		WebDriver window3 = windows.get(2);


		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = applicationContext.getBean(LoginPage.class, window2, 1);
		loginPage2.enterUsername(users.get(1));
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		LoginPage loginPage3 = applicationContext.getBean(LoginPage.class, window3, 2);
		loginPage3.enterUsername(users.get(2));
		loginPage3.enterPassword("cascaded");
		loginPage3.login();


		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.selectHatchback();

		HomePage homePage2 = applicationContext.getBean(HomePage.class, window2, 1);
		homePage2.addPlace("Dr Ivana Ribara 13");
		homePage2.addPlace("Baranjska 5");
		homePage2.selectHatchback();

		homePage1.orderRide();
		homePage2.orderRide();

		RideDetailsPage rideDetailsPage1 = new RideDetailsPage(window1);
		RideDetailsPage rideDetailsPage2 = new RideDetailsPage(window2);

		boolean loaded1 = rideDetailsPage1.optionalHeader();
		boolean loaded2 = rideDetailsPage2.optionalHeader();

		assert ((loaded1 && !loaded2) || (!loaded1 && loaded2));
	}


	@Test
	@Rollback
	public void Order_fails_no_funds() {
		BasePage.testName = "Order_fails_no_funds";
		List<String> users = List.of("zdravko.zdravkovic@gmail.com", "dzamal.malik@gmail.com");
		createDrivers(2, users);
		WebDriver window1 = windows.get(0);
		WebDriver window2 = windows.get(1);

		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = applicationContext.getBean(LoginPage.class, window2, 1);
		loginPage2.enterUsername(users.get(1));
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window2, 1);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.selectHatchback();
		homePage1.orderRide();
		String result = homePage1.waitToastError();
		assertEquals("Insufficient funds.", result);
	}


	@Test
	@Disabled
	@Rollback
	public void Pal_has_insufficient_funds() {
		BasePage.testName = "Pal_has_insufficient_funds";
		List<String> users = List.of("branimir.branimirovic@gmail.com", "dzamal.malik@gmail.com", "zdravko.zdravkovic@gmail.com");
		createDrivers(3, users);

		WebDriver window1 = windows.get(0);
		WebDriver window2 = windows.get(1);
		WebDriver window3 = windows.get(2);

		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		LoginPage loginPage2 = applicationContext.getBean(LoginPage.class, window2, 1);
		loginPage2.enterUsername(users.get(1));
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		LoginPage loginPage3 = applicationContext.getBean(LoginPage.class, window3, 2);
		loginPage3.enterUsername(users.get(2));
		loginPage3.enterPassword("cascaded");
		loginPage3.login();

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.addPal(users.get(1));
		homePage1.selectHatchback();
		homePage1.orderRide();

		HomePage homePage2 = applicationContext.getBean(HomePage.class, window2, 1);
		homePage2.acceptFirstRideInvitation();
		homePage2.waitToastError();
	}

	@Test
	@Rollback
	public void Pal_accepts_ride() {
		BasePage.testName = "Pal_accepts_ride";
		List<String> users = List.of("branimir.branimirovic@gmail.com" ,"andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com");
		createDrivers(3, users);

		WebDriver window1 = windows.get(0);
		WebDriver window2 = windows.get(1);
		WebDriver window3 = windows.get(2);

		LoginPage loginPage1 = applicationContext.getBean(LoginPage.class, window1, 0);
		loginPage1.enterUsername(users.get(0));
		loginPage1.enterPassword("cascaded");
		loginPage1.login();

		System.out.println(window1.getCurrentUrl());

		LoginPage loginPage2 = applicationContext.getBean(LoginPage.class, window2, 1);
		loginPage2.enterUsername(users.get(1));
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		LoginPage loginPage3 = applicationContext.getBean(LoginPage.class, window3, 2);
		loginPage3.enterUsername(users.get(2));
		loginPage3.enterPassword("cascaded");
		loginPage3.login();

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.addPal(users.get(1));
		homePage1.selectHatchback();
		homePage1.orderRide();

		sleep(1000);
		HomePage homePage2 = new HomePage(window2, 1);
		homePage2.acceptFirstRideInvitation();
		RideDetailsPage rideDetailsPage = new RideDetailsPage(window2);
		boolean accepted = rideDetailsPage.optionalHeader();
		assert accepted;
//		close = false;
	}

}
