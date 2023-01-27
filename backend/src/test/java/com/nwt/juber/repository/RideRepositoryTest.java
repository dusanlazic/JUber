package com.nwt.juber.repository;

import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideStatus;
import com.nwt.juber.service.FileStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
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
	EntityManager entityManager;

	@ParameterizedTest
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

	@ParameterizedTest
	@MethodSource("activeRideDriverProvider")
	@DisplayName("Test getting active ride for driver")
	public void Driver_has_valid_active_ride(UUID driverId, UUID rideId) {
		Ride ride = rideRepository.getActiveRideForDriver(driverId);
		System.out.println(ride);
		assertEquals(rideId, ride.getId());
		RideStatus status = ride.getRideStatus();
		assert ride.getRideStatus() == RideStatus.ACCEPTED || ride.getRideStatus() == RideStatus.IN_PROGRESS || ride.getRideStatus() == RideStatus.WAIT;
		assert ride.getDriver().getId().equals(driverId);
		assert ride.getStartTime() == null;
		assert ride.getEndTime() == null;
		for(Ride r : rideRepository.findAll()) {
			if (r.getDriver().getId().equals(driverId) && !r.getId().equals(rideId)) {
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

	@ParameterizedTest
	@MethodSource("activeRidePassengerProvider")
	@DisplayName("Test getting active ride for passenger")
	public void Passenger_has_valid_active_ride(UUID passengerId, UUID rideId) {
		Passenger passenger = passengerRepository.findById(passengerId).get();
		Ride ride = rideRepository.getActiveRideForPassenger(passenger);
		System.out.println(ride);
		assertEquals(rideId, ride.getId());
		assert ride.getRideStatus() == RideStatus.ACCEPTED || ride.getRideStatus() == RideStatus.IN_PROGRESS;
		assert ride.getPassengers().stream().anyMatch(p -> p.getId().equals(passengerId));
		assert ride.getStartTime() == null;
		assert ride.getEndTime() == null;
		for(Ride r : rideRepository.findAll()) {
			if (r.getPassengers().contains(passenger) && !r.getId().equals(rideId)) {
				assert r.getRideStatus().ordinal() > RideStatus.IN_PROGRESS.ordinal();
			}
		}
	}

	static List<Arguments> activeRidePassengerProvider() {
		return List.of(
				arguments(Constants.DRIVER_ZDRAVKO_ID, "3afa6238-862b-417b-9a88-fbf2bc90c09d"), // zdravko has only one active ride
				arguments(Constants.DRIVER_MARKO_ID, "2ac4bc01-6326-418f-a3f9-4244e3922439"), // marko has two on wait, gets the first one
				arguments(Constants.DRIVER_NIKOLA_ID, "060443d3-abc6-458c-9f76-92e3de51a713") // nikola has one active and one scheduled, should return active
		);
	}


	@Test
	@DisplayName("Test getting ride by id")
	public void Ride_is_valid(String rideId) {
		UUID id = UUID.fromString(rideId);
		Ride ride = rideRepository.findById(id).get();
		assertEquals(id, ride.getId());
		assertEquals(RideStatus.ACCEPTED, ride.getRideStatus());
		assertEquals(2, ride.getPassengers().size());
	}






}

