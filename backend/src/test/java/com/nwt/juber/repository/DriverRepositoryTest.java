package com.nwt.juber.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.nwt.juber.dto.DriverRideDTO;
import com.nwt.juber.dto.message.PersonLocationMessage;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverStatus;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideStatus;
import com.nwt.juber.service.FileStorageService;

import javax.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class DriverRepositoryTest {

	@MockBean
	private FileStorageService fileStorageService;

	@Autowired
	private DriverRepository driverRepository;


	@Autowired
	private EntityManager entityManager;

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
		driverRepository.setDriverStatus(DriverStatus.ACTIVE, "active.unavailable@gmail.com");
		entityManager.flush();
		entityManager.clear();
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

		drivers.forEach(d -> System.out.println(d.getEmail()));

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
				arguments(DriverStatus.ACTIVE, 0),
				arguments(DriverStatus.INACTIVE, 6),
				arguments(DriverStatus.OVERTIME, 1)
		);
	}
	
	// has ride in status: WAIT, ACCEPTED or IN_PROGRESS
	@ParameterizedTest(name = "Finding ride in status valid for simulation by driver email {0}")
	@MethodSource("rideForSimulationProvider")
	public void findRideForSimulation(String email, UUID rideId) {
		List<Ride> rides = driverRepository.findRideForSimulation(email);
		assert !rides.isEmpty();
		for (Ride ride2 : rides) {
			assertEquals(ride2.getId(), rideId);
			assertTrue(ride2.getRideStatus().equals(RideStatus.WAIT) || 
					ride2.getRideStatus().equals(RideStatus.ACCEPTED) || 
					ride2.getRideStatus().equals(RideStatus.IN_PROGRESS));
		}
	}

	static List<Arguments> rideForSimulationProvider() {
		return List.of(
				arguments("zdravko.zdravkovic@gmail.com", Constants.RIDE_3),
				arguments("nikola.nikolic@gmail.com", Constants.RIDE_6),
				arguments("dusan.dusanovic@gmail.com", Constants.RIDE_8)
				
		);
	}
	
	// does not have ride in status: WAIT, ACCEPTED or IN_PROGRESS
	@ParameterizedTest(name = "Not finding ride in status valid for simulation by driver email {0}")
	@ValueSource(strings = {"branko.brankovic@gmail.com"})
	public void notFoundRideForSimulation(String email) {
		List<Ride> ride = driverRepository.findRideForSimulation(email);
		assert ride.isEmpty();
	}
	
    @ParameterizedTest(name = "Finding unavailable drivers with no future rides")
    @MethodSource("unavailableDriversWithNoFutureRidesProvider")
	public void findUnavailableDriversWithNoFutureRides(List<String> drivers) {

		List<DriverStatus> statuses = drivers.stream().map(d -> driverRepository.findByEmail(d).get().getStatus()).toList();
		drivers.forEach(d -> driverRepository.setDriverStatus(DriverStatus.ACTIVE, d));
		entityManager.flush();
		entityManager.clear();
		List<DriverRideDTO> dtos = driverRepository.findUnavailableDriversWithNoFutureRides();
		assertEquals(drivers.size(), dtos.size());
		for (DriverRideDTO dto : dtos) {
			assert drivers.contains(dto.getDriver().getEmail());
			assert dto.getDriver().getStatus().equals(DriverStatus.ACTIVE);
			assert dto.getRide().getRideStatus().equals(RideStatus.WAIT) || 
				   dto.getRide().getRideStatus().equals(RideStatus.ACCEPTED) || 
				   dto.getRide().getRideStatus().equals(RideStatus.IN_PROGRESS);
			for (Ride ride : dto.getDriver().getRides()) {
				assertFalse(ride.getRideStatus().equals(RideStatus.SCHEDULED));
			}
		}

		for (int i = 0; i < drivers.size(); i++) {
			driverRepository.setDriverStatus(statuses.get(i), drivers.get(i));
		}
	}
	
	static List<Arguments> unavailableDriversWithNoFutureRidesProvider() {
		return List.of( 
				arguments(List.of( "active.unavailable@gmail.com"))
		);
	}
}
