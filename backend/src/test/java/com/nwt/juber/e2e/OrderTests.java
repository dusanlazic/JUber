package com.nwt.juber.e2e;

import com.nwt.juber.config.TestConfig;
import com.nwt.juber.e2e.pages.HomePage;
import com.nwt.juber.e2e.pages.LoginPage;
import com.nwt.juber.model.Ride;
import com.nwt.juber.repository.RideRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles("test")
public class OrderTests {

	@Autowired
	private RestTemplateBuilder builder;

	@Autowired
	private RideRepository rideRepository;

	List<WebDriver> windows = new ArrayList<>();

	public WebDriver openDriver(int number) {
		WebDriver driver;
		System.setProperty("webdriver.chrome.driver", "chromedriver");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		options.addArguments("--user-data-dir=/tmp/tmp" + number);
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		return driver;
	}

	public void createDrivers(int number) {
		for (int i = 0; i < number; i++) {
			WebDriver driver = openDriver(i);
			windows.add(driver);
		}
	}

	@Test
	public void Order_is_successful() {
		createDrivers(2);
		WebDriver window1 = windows.get(1);
		WebDriver window2 = windows.get(0);


		LoginPage loginPage1 = new LoginPage(window1);
		loginPage1.enterUsername("andrej.andrejevic@gmail.com");
		loginPage1.enterPassword("cascaded");
		sleep(2000);
		loginPage1.login();

		LoginPage loginPage2 = new LoginPage(window2);
		loginPage2.enterUsername("zdravko.zdravkovic@gmail.com");
		loginPage2.enterPassword("cascaded");
		loginPage2.login();

		HomePage homePage1 = new HomePage(window1);
		homePage1.addPlace("Dr Ivana Ribara 13");
		homePage1.addPlace("Baranjska 5");
//		homePage1.addPal("mile.miletic@gmail.com");
		homePage1.selectHatchback();
		homePage1.orderRide();


		List<Ride> rides = rideRepository.findAll();
		System.out.println(rides.size());
		for (Ride ride : rides) {
			System.out.println(ride.getId() + " " + ride.getDriver().getEmail() + " " + ride.getRideStatus());
		}
		sleep(10000);
	}

	public void sleep(int millies) {
		try {
			Thread.sleep(millies);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

//	@AfterEach
//	public void close() {
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		for (WebDriver window : windows) {
//			window.close();
//		}
//	}



}
