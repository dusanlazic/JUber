package com.nwt.juber.repository;

import com.nwt.juber.dto.DriverRideDTO;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideStatus;
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
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@ActiveProfiles("test")
public class RideRepositoryTest {

	@Autowired
	private RideRepository rideRepository;


	@MockBean
	private FileStorageService fileStorageService;

	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	private DriverRepository driverRepository;


	@Autowired
	EntityManager entityManager;

	@ParameterizedTest(name = "Updating status to {0}")
	@MethodSource(value = "rideStatusProvider")
	@DisplayName("Test updating staus of ride")
	public void Ride_changes_status(RideStatus status) {
		UUID id = UUID.fromString("8107614c-04d9-480d-8a59-e1999d9e7bfc");
		Ride ride = rideRepository.findById(id).get();
		rideRepository.setRideStatus(ride.getId(), status);
		entityManager.flush();
		entityManager.clear();
		RideStatus newStatus = rideRepository.findById(id).get().getRideStatus();
		assertEquals(status, newStatus);
	}

	static List<RideStatus> rideStatusProvider() {
		return Arrays.asList(RideStatus.values());
	}

	@ParameterizedTest(name = "Finding valid active ride for driver {0}")
	@MethodSource("activeRideDriverProvider")
	@DisplayName("Test getting active ride for driver")
	public void Driver_has_valid_active_ride(UUID driverId, UUID rideId) {
		Ride ride = rideRepository.getActiveRideForDriver(driverId);
		assertEquals(rideId, ride.getId());
		RideStatus status = ride.getRideStatus();
		assert ride.getRideStatus() == RideStatus.ACCEPTED || ride.getRideStatus() == RideStatus.IN_PROGRESS || ride.getRideStatus() == RideStatus.WAIT;
		assert ride.getDriver().getId().equals(driverId);
//		assert ride.getStartTime() == null;
		assert ride.getEndTime() == null;
		for(Ride r : rideRepository.findAll()) {
			if (r.getDriver() != null && r.getDriver().getId().equals(driverId) && !r.getId().equals(rideId)) {
				if (r.getRideStatus().ordinal() <= RideStatus.IN_PROGRESS.ordinal()) {
					assert r.getRideStatus().ordinal() <= status.ordinal();
				}
			}
		}
	}

	static List<Arguments> activeRideDriverProvider() {
		return List.of(
			arguments(Constants.DRIVER_ZDRAVKO_ID, "3afa6238-862b-417b-9a88-fbf2bc90c09d"), // zdravko has only one active ride
			arguments(Constants.DRIVER_MARKO_ID, "2ac4bc01-6326-418f-a3f9-4244e3922439"), // marko has two on wait, gets the first one
			arguments(Constants.DRIVER_NIKOLA_ID, "060443d3-abc6-458c-9f76-92e3de51a713") // nikola has one active and one scheduled, should return active
		);
	}

	@ParameterizedTest(name = "Getting active ride for passenger {0}")
	@MethodSource("activeRidePassengerProvider")
	@DisplayName("Test getting active ride for passenger")
	public void Passenger_has_valid_active_ride(UUID passengerId, UUID rideId) {
		Passenger passenger = passengerRepository.findById(passengerId).get();
		Ride ride = rideRepository.getActiveRideForPassenger(passenger);
		assertEquals(rideId, ride.getId());
		assert ride.getRideStatus() == RideStatus.ACCEPTED || ride.getRideStatus() == RideStatus.IN_PROGRESS;
		assert ride.getPassengers().stream().anyMatch(p -> p.getId().equals(passengerId));
//		assert ride.getStartTime() == null;
		assert ride.getEndTime() == null;
		for(Ride r : rideRepository.findAll()) {
			if (r.getPassengers().contains(passenger) && !r.getId().equals(rideId)) {
				assert r.getRideStatus().ordinal() > RideStatus.IN_PROGRESS.ordinal();
			}
		}
	}

	static List<Arguments> activeRidePassengerProvider() {
		return List.of(
				arguments(Constants.PASSENGER_MILE_ID, "3afa6238-862b-417b-9a88-fbf2bc90c09d"), // mile rides with zdravko
				arguments(Constants.PASSENGER_PETAR_ID, "2ac4bc01-6326-418f-a3f9-4244e3922439"), // petar rides with marko
				arguments(Constants.PASSENGER_DRAGAN_ID, "060443d3-abc6-458c-9f76-92e3de51a713") // dragan with nikola
		);
	}


	@ParameterizedTest(name = "Finidng ride {0}")
	@ValueSource(strings = {"3afa6238-862b-417b-9a88-fbf2bc90c09d", "2ac4bc01-6326-418f-a3f9-4244e3922439", "060443d3-abc6-458c-9f76-92e3de51a713"})
	@DisplayName("Test getting ride by id")
	public void Finding_ride(String rideId) {
		UUID id = UUID.fromString(rideId);
		Ride ride = rideRepository.findById(id).get();
		assertEquals(id, ride.getId());
	}

	@ParameterizedTest(name = "Getting scheduled ride for driver {0}")
	@MethodSource("scheduledRideDriverProvider")
	@DisplayName("Test getting scheduled ride for driver")
	public void Driver_has_valid_scheduled_ride(UUID driverId, UUID rideId, int total) {
		Driver driver = driverRepository.findById(driverId).get();
		Ride ride = rideRepository.getScheduledRideForDriver(driver);
		if (total == 0) {
			assert ride == null;
			return;
		}
		assertEquals(rideId, ride.getId());
		assert ride.getRideStatus() == RideStatus.SCHEDULED;
		assert ride.getDriver().getId().equals(driverId);
		assert ride.getEndTime() == null;
		for(Ride r : rideRepository.findAll()) {
			if (r.getDriver() != null && r.getDriver().getId().equals(driverId) && !r.getId().equals(rideId)) {
				assert r.getRideStatus().ordinal() < RideStatus.SCHEDULED.ordinal();
			}
		}
	}

	static List<Arguments> scheduledRideDriverProvider() {
		return List.of(
				arguments(Constants.DRIVER_ZDRAVKO_ID, "3afa6238-862b-417b-9a88-fbf2bc90c09d", 0),
				arguments(Constants.DRIVER_MARKO_ID, "2ac4bc01-6326-418f-a3f9-4244e3922439", 0),
				arguments(Constants.DRIVER_NIKOLA_ID, "7a1255b3-e69d-40f5-990d-bdfbe60e8258", 1)
		);
	}

	@Test
	@DisplayName("Test finding unavailable drivers with no future rides")
	public void Finding_unavailable_drivers_with_no_future_rides() {
		List<DriverRideDTO> drivers = driverRepository.findUnavailableDriversWithNoFutureRides();
		for (DriverRideDTO driver : drivers) {
			System.out.println(driver);
		}
		assert drivers.size() == 0;
	}

	@ParameterizedTest
	@MethodSource("pastRidesPassengerProvider")
	@DisplayName("Test finding past rides of a passenger")
	public void Finding_past_rides_of_a_passenger(UUID passengerId, int count) {
		Passenger passenger = passengerRepository.findById(passengerId).get();
		List<Ride> rides = rideRepository.getPastRidesForPassenger(passenger);
		assertEquals(count, rides.size());
	}

	static List<Arguments> pastRidesPassengerProvider() {
		return List.of(
				arguments(Constants.PASSENGER_MILE_ID, 2),
				arguments(Constants.PASSENGER_PETAR_ID, 4),
				arguments(Constants.PASSENGER_DRAGAN_ID, 2)
		);
	}

	@ParameterizedTest(name = "Finding finished rides that ended between {0} and {1}")
	@MethodSource("ridesBetweenTimesDateProvider")
	public void Finding_rides_between_times(LocalDateTime startTime, LocalDateTime endTime, int count) {
		List<Ride> rides = rideRepository.getRidesBetweenTimes(startTime, endTime);
		assertEquals(count, rides.size());
	}

	static List<Arguments> ridesBetweenTimesDateProvider() {
		return List.of(
				arguments(
						LocalDateTime.of(2023, 5, 22, 0, 0),
						LocalDateTime.of(2023, 5, 25, 0, 0),
						3
				),
				arguments(
						LocalDateTime.of(2023, 5, 24, 0, 0),
						LocalDateTime.of(2023, 5, 28, 0, 0),
						4
				),
				arguments(
						LocalDateTime.of(1970, 1, 1, 0, 0),
						LocalDateTime.of(2028, 5, 25, 0, 0),
						8
				)
		);
	}





}

