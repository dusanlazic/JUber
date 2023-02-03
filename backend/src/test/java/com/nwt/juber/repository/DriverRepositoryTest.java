package com.nwt.juber.repository;


import com.nwt.juber.dto.message.PersonLocationMessage;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverStatus;
import com.nwt.juber.service.FileStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@ActiveProfiles("test")
public class DriverRepositoryTest {

	@Autowired
	private RideRepository rideRepository;

	@MockBean
	private FileStorageService fileStorageService;

	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	private DriverRepository driverRepository;

	@ParameterizedTest(name = "Finding driver by email {0}")
	@ValueSource(strings = {"zdravko.zdravkovic@gmail.com", "nikola.nikolic@gmail.com"})
	public void Driver_with_email_exists(String email) {
		Driver driver = driverRepository.findByEmail(email).get();
		assert driver.getEmail().equals(email);
	}

	@ParameterizedTest(name = "Finding driver by email {0}")
	@ValueSource(strings = {"petar.petrovic@gmail.com", "mile.miletic@gmail.com"})
	public void No_driver_with_email(String email) {
		Optional<Driver> driver = driverRepository.findByEmail(email);
		assert driver.isEmpty();
	}

	@Test
	@DisplayName("Test finding all locations of drivers")
	public void Find_all_locations_of_drivers() {
		List<PersonLocationMessage> locations = driverRepository.findAllLocations();
		System.out.println(locations);
		assertEquals(1, locations.size());
	}

	// parametrized test for locationForDriverEmail method in DriverRepository
	@ParameterizedTest(name = "Finding location for driver with email {0}")
	@MethodSource(value = "locationForDriverEmailProvider")
	public void Location_for_driver_email(String email, Double latitude, Double longitude) {
		PersonLocationMessage locationForDriverEmail = driverRepository.locationForDriverEmail(email);
		assert locationForDriverEmail.getEmail().equals(email);
		assert locationForDriverEmail.getLatitude().equals(latitude);
		assert locationForDriverEmail.getLongitude().equals(longitude);
	}

	static List<Arguments> locationForDriverEmailProvider() {
		return List.of(
				arguments("zdravko.zdravkovic@gmail.com", 45.246, 19.8512),
				arguments("marko.markovic@gmail.com", 45.246, 19.8513),
				arguments("nikola.nikolic@gmail.com", 45.246, 19.8514)
		);
	}

	@Test
	public void Finding_available_drivers() {
		List<Driver> drivers = driverRepository.findAvailableDrivers();
		assertEquals(0, drivers.size());
	}

	@ParameterizedTest
	@MethodSource("driverStatusProvider")
	public void Finding_by_status(DriverStatus status, int count) {
		List<Driver> drivers = driverRepository.findByStatus(status);
		assertEquals(count, drivers.size());
	}

	static List<Arguments> driverStatusProvider() {
		return List.of(
				arguments(DriverStatus.DRIVING, 0),
				arguments(DriverStatus.ACTIVE, 1),
				arguments(DriverStatus.INACTIVE, 3),
				arguments(DriverStatus.OVERTIME, 1)
		);
	}

}
