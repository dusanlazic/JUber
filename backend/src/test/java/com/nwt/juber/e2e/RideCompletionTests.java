package com.nwt.juber.e2e;

import com.nwt.juber.e2e.pages.HomePage;
import com.nwt.juber.e2e.pages.LoginPage;
import com.nwt.juber.e2e.pages.RideDetailsPage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class RideCompletionTests extends TestBase {


	@Autowired
	ApplicationContext applicationContext;

	@Test
	@Rollback
	public void Ride_is_ordered_and_completed_successfully() {
		BasePage.testName = "Ride_is_ordered_and_completed_successfully";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "goran.goranov@gmail.com");

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

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.selectHatchback();
		homePage1.orderRide();

		RideDetailsPage rideDetailsPagePassenger = new RideDetailsPage(window1);
		RideDetailsPage rideDetailsPageDriver = new RideDetailsPage(window2);

		// Accept ride
		rideDetailsPageDriver.acceptRideDriver();
		assertEquals("Goran Goranov", rideDetailsPagePassenger.getDriverFullName());

		// Passengers got in
		rideDetailsPageDriver.passengersGotIn();
		rideDetailsPagePassenger.waitPanicButton();

		// Ride is finished
		rideDetailsPageDriver.rideFinishedDriver();
		rideDetailsPageDriver.waitRideStatusNone();
		rideDetailsPagePassenger.waitRideStatusFinished();
	}

	@Test
	@Rollback
	public void Driver_declines_ride() {
		BasePage.testName = "Ride_is_ordered_and_completed_successfully";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "goran.goranov@gmail.com");

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

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.selectHatchback();
		homePage1.orderRide();

		RideDetailsPage rideDetailsPagePassenger = new RideDetailsPage(window1);
		RideDetailsPage rideDetailsPageDriver = new RideDetailsPage(window2);

		// Decline ride
		rideDetailsPageDriver.declineRideDriver();
		rideDetailsPageDriver.waitRideStatusNone();
		rideDetailsPagePassenger.waitRideStatusFailed();
	}

	@Test
	@Rollback
	public void Driver_abandons_ride() {
		BasePage.testName = "Driver_abandons_ride";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "goran.goranov@gmail.com");

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

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.selectHatchback();
		homePage1.orderRide();

		RideDetailsPage rideDetailsPagePassenger = new RideDetailsPage(window1);
		RideDetailsPage rideDetailsPageDriver = new RideDetailsPage(window2);

		// Accept ride
		rideDetailsPageDriver.acceptRideDriver();
		assertEquals("Goran Goranov", rideDetailsPagePassenger.getDriverFullName());

		// Abandon ride
		rideDetailsPageDriver.abandonRideDriver();
		rideDetailsPagePassenger.waitRideStatusFailed();
	}

	@Test
	@Rollback
	public void Passenger_clicks_panic() {
		BasePage.testName = "Passenger_clicks_panic";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "goran.goranov@gmail.com");

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

		HomePage homePage1 = applicationContext.getBean(HomePage.class, window1, 0);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
		homePage1.selectHatchback();
		homePage1.orderRide();

		RideDetailsPage rideDetailsPagePassenger = new RideDetailsPage(window1);
		RideDetailsPage rideDetailsPageDriver = new RideDetailsPage(window2);

		// Accept ride
		rideDetailsPageDriver.acceptRideDriver();
		assertEquals("Goran Goranov", rideDetailsPagePassenger.getDriverFullName());

		// Passengers got in
		rideDetailsPageDriver.passengersGotIn();
		rideDetailsPagePassenger.waitPanicButton();

		// Passenger clicks panic
		rideDetailsPagePassenger.panic();
		rideDetailsPagePassenger.waitRideStatusNone();
	}

}