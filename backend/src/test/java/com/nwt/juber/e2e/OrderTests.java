package com.nwt.juber.e2e;

import com.nwt.juber.e2e.pages.HomePage;
import com.nwt.juber.e2e.pages.LoginPage;
import com.nwt.juber.e2e.pages.RideDetailsPage;
import com.nwt.juber.repository.PassengerRepository;
import org.junit.jupiter.api.*;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class OrderTests extends TestBase {


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
	@Order(1)
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

		sleep(2000);
		HomePage homePage2 = applicationContext.getBean(HomePage.class, window2, 1);
		homePage2.acceptFirstRideInvitation();
		RideDetailsPage rideDetailsPage = new RideDetailsPage(window2);
		boolean accepted = rideDetailsPage.optionalHeader();
		assert accepted;
//		close = false;
	}


	@Test
	@Rollback
	public void Pal_declines_ride() {
		BasePage.testName = "Pal_declines_ride";
		List<String> users = List.of("branimir.branimirovic@gmail.com" ,"andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com");
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

		sleep(2000);
		HomePage homePage2 = new HomePage(window2, 1);
		homePage2.declineFirstRideInvitation();
		RideDetailsPage rideDetailsPage = new RideDetailsPage(window1);
		rideDetailsPage.waitRideStatusFailed();
//		close = false;
	}

	@Test
	@Rollback
	public void Driver_declines_ride_and_no_drivers_are_left() {
		BasePage.testName = "Driver_declines_ride_and_no_drivers_are_left";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com");

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

		RideDetailsPage rideDetailsPage = new RideDetailsPage(window2);
		rideDetailsPage.declineRideDriver();

		homePage1.waitToastError();
	}

	@Test
	@Rollback
	public void Driver_declines_ride_and_drivers_other_drivers_are_occupied() {
		BasePage.testName = "Driver_declines_ride_and_drivers_are_left";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com", "marko.markovic@gmail.com");

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
		homePage1.orderRide();

		RideDetailsPage rideDetailsPage = new RideDetailsPage(window2);
		rideDetailsPage.declineRideDriver();

		homePage1.waitToastError();

	}


	@Test
	@Order(0)
	@Rollback
	public void Driver_declines_ride_and_ride_is_assigned_to_another_driver() {
		BasePage.testName = "Driver_declines_ride_and_drivers_are_left";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com", "dusan.dusanovic@gmail.com");

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
		homePage1.orderRide();

		RideDetailsPage rideDetailsPage = new RideDetailsPage(window2);
		rideDetailsPage.declineRideDriver();

		RideDetailsPage rideDetailsPage2 = new RideDetailsPage(window3);
		rideDetailsPage2.waitForPassengerToAppear("Andrej Andrejevic");
	}


	@Test
	@Rollback
	public void Schedueld_ride_is_more_than_5_hours_from_now() {
		BasePage.testName = "Schedueld_ride_is_more_than_5_hours_from_now";
		List<String> users = List.of("andrej.andrejevic@gmail.com", "zdravko.zdravkovic@gmail.com");

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

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime fiveHoursFromNow = now.plusHours(5).plusMinutes(2);
		String hours = String.valueOf(fiveHoursFromNow.getHour());
		String minutes = String.valueOf(fiveHoursFromNow.getMinute());
		System.out.println("SADA JE VREME");
		System.out.println(hours + " " + minutes);
		homePage1.setScheduledTime(hours, minutes);

		homePage1.orderRide();
		String toastMessage = homePage1.waitToastError();
		assertEquals("Field validation failed.", toastMessage);
	}


}
