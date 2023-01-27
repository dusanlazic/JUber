package com.nwt.juber.service;

import com.nwt.juber.dto.DriverRideDTO;
import com.nwt.juber.exception.EndRideException;
import com.nwt.juber.exception.StartRideException;
import com.nwt.juber.model.*;
import com.nwt.juber.repository.DriverRepository;
import com.nwt.juber.repository.RideRepository;
import com.nwt.juber.repository.VehicleTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RideServiceTest {

	@Mock
	private RideRepository rideRepository;

	@Mock
	private DriverRepository driverRepository;

	@Mock
	private TimeEstimator timeEstimator;

	@Mock
	private SimpMessagingTemplate simpMessagingTemplate;

	@InjectMocks
	private RideService rideService;

	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;


	@BeforeEach
	public void setUp() {

	}

	@Test
	@DisplayName("Starting valid ride")
	public void Start_valid_ride() {
		// given
		UUID rideId = UUID.randomUUID();
		Ride ride = new Ride();
		this.initRide(ride);
		ride.setId(rideId);
		ride.setRideStatus(RideStatus.ACCEPTED);

		when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
		when(rideRepository.save(any(Ride.class))).thenReturn(ride);
		when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		rideService.startRide(rideId);

		// then
		assertEquals(RideStatus.IN_PROGRESS, ride.getRideStatus());
		assertNotNull(ride.getStartTime());
		assertTrue(ride.getStartTime().isBefore(LocalDateTime.now().plusSeconds(1)));
		assertTrue(ride.getStartTime().isAfter(LocalDateTime.now().minusSeconds(1)));
		verify(rideRepository).save(ride);
		//	verify(rideService).sendRideMessageToPassengers(ride, RideMessageType.DRIVER_ARRIVED);
	}

	private void initRide(Ride ride) {
		ride.setPlaces(new ArrayList<>());
		ride.setPassengers(new ArrayList<>());
		ride.setBlacklisted(new ArrayList<>());
		ride.setPassengersReady(new ArrayList<>());
	}

	@Test
	@DisplayName("Start ride with invalid status")
	public void Start_ride_that_is_not_accepted() {
		// given
		UUID rideId = UUID.randomUUID();
		Ride ride = new Ride();
		ride.setId(rideId);
		ride.setRideStatus(RideStatus.WAIT);
		when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		StartRideException exception = assertThrows(StartRideException.class, () -> rideService.startRide(rideId));

		// then
		assertEquals("Ride not accepted!", exception.getMessage());
		verify(rideRepository, never()).save(ride);
		// verify(rideService, never()).sendRideMessageToPassengers(any(), any());
	}


	@Test
	@DisplayName("Start ride that does not exist")
	public void Start_nonexistent_ride() {
		// given
		UUID rideId = UUID.randomUUID();
		when(rideRepository.findById(rideId)).thenReturn(Optional.empty());

		// when
		StartRideException exception = assertThrows(StartRideException.class, () -> rideService.startRide(rideId));

		// then
		assertEquals("No ride with id: " + rideId.toString(), exception.getMessage());
		verify(rideRepository, never()).save(any());
		// verify(rideService, never()).sendRideMessageToPassengers(any(), any());
	}

	@Test
	@DisplayName("Ending a valid ride")
	public void Ending_valid_ride() {
		// given
		UUID rideId = UUID.randomUUID();
		Ride ride = new Ride();
		this.initRide(ride);
		ride.setId(rideId);
		ride.setRideStatus(RideStatus.IN_PROGRESS);
		when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		rideService.endRide(rideId);

		// then
		assertEquals(RideStatus.FINISHED, ride.getRideStatus());
		assertNotNull(ride.getEndTime());
		verify(rideRepository).save(ride);
		verify(rideRepository).findById(rideId);
	}

	@Test
	@DisplayName("Ending an invalid ride")
	public void Ending_ride_that_is_not_in_progress() {
		// given
		UUID rideId = UUID.randomUUID();
		Ride ride = new Ride();
		ride.setId(rideId);
		ride.setRideStatus(RideStatus.ACCEPTED);
		when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		assertThrows(StartRideException.class, () -> rideService.endRide(rideId));
	}

	@Test
	@DisplayName("Ending ride that does not exist")
	public void End_nonexistent_ride() {
		// setup
		UUID rideId = UUID.randomUUID();
		when(rideRepository.findById(rideId)).thenReturn(Optional.empty());

		// when
		assertThrows(EndRideException.class, () -> rideService.endRide(rideId));
	}


	@Test
	@DisplayName("Finding closes available driver")
	void Find_closest_available_driver() {
		// given
		VehicleType vehicleType = new VehicleType();
		Ride ride = new Ride();
		ride.setVehicleTypeRequested(vehicleType);
		this.initRide(ride);
		Place startPlace = new Place();
		startPlace.setLatitude(1.0);
		startPlace.setLongitude(1.0);
		List<Place> places = new ArrayList<>();
		places.add(startPlace);
		ride.setPlaces(places);

		Driver driver1 = new Driver();
		driver1.setBlocked(false);
		Vehicle vehicle1 = new Vehicle();
		vehicle1.setCapacity(4);
		vehicle1.setVehicleType(vehicleType);
		vehicle1.setLatitude(2.0);
		vehicle1.setLongitude(2.0);
		driver1.setVehicle(vehicle1);

		Driver driver2 = new Driver();
		Vehicle vehicle2 = new Vehicle();
		vehicle2.setCapacity(4);
		vehicle2.setLatitude(3.0);
		vehicle2.setVehicleType(vehicleType);
		vehicle2.setLongitude(3.0);
		driver2.setBlocked(false);
		driver2.setVehicle(vehicle2);

		List<Driver> drivers = new ArrayList<>();
		drivers.add(driver1);
		drivers.add(driver2);

		when(driverRepository.findAvailableDrivers(ride)).thenReturn(drivers);

		// when
		Driver closestDriver = rideService.findClosestAvailableDriver(ride);

		// then
		assertEquals(driver1, closestDriver);
	}


	@ParameterizedTest
	@MethodSource("fastestUnavailableSource")
	public void Find_fastest_unavailable(int duration1, int duration2, int estimation1, int estimation2, int shouldWin) {

		// given
		VehicleType vehicleType = new VehicleType();
		Ride ride = new Ride();
		ride.setVehicleTypeRequested(vehicleType);
		initRide(ride);
		List<Place> places1 = new ArrayList<>();
		Place place1 = new Place();
		place1.setLatitude(1.0);
		place1.setLongitude(1.0);
		places1.add(place1);

		List<Place> places2 = new ArrayList<>();
		Place place2 = new Place();
		place2.setLatitude(2.0);
		place2.setLongitude(2.0);
		places2.add(place2);

		ride.setPlaces(places1);

		LocalDateTime now = LocalDateTime.now().minusSeconds(30);

		Ride ride1 = new Ride();
		ride1.setStartTime(now);
		ride1.setDuration(duration1);
		ride1.setPlaces(places1);

		Ride ride2 = new Ride();
		ride2.setStartTime(now);
		ride2.setDuration(duration2);
		ride2.setPlaces(places2);

		Driver driver1 = new Driver();
		driver1.setBlocked(false);
		Vehicle vehicle1 = new Vehicle();
		vehicle1.setVehicleType(vehicleType);
		vehicle1.setCapacity(4);
		driver1.setVehicle(vehicle1);

		Driver driver2 = new Driver();
		driver2.setBlocked(false);
		Vehicle vehicle2 = new Vehicle();
		vehicle2.setVehicleType(vehicleType);
		vehicle2.setCapacity(4);
		driver2.setVehicle(vehicle2);

		DriverRideDTO driverRideDTO1 = new DriverRideDTO(driver1, ride1);
		DriverRideDTO driverRideDTO2 = new DriverRideDTO(driver2, ride2);
		List<DriverRideDTO> drivers = Arrays.asList(driverRideDTO1, driverRideDTO2);

		when(driverRepository.findUnavailableDriversWithNoFutureRides(ride)).thenReturn(drivers);
		when(timeEstimator.estimateTime(eq(place1.getLatitude()), eq(place1.getLongitude()),
													anyDouble(), anyDouble())).thenReturn(estimation1);

		when(timeEstimator.estimateTime(eq(place2.getLatitude()), eq(place2.getLongitude()),
													anyDouble(), anyDouble())).thenReturn(estimation2);
		// when
		Driver result = rideService.findFastestUnavailable(ride);

		// then
		assertNotNull(result);
		if(shouldWin == 1) {
			assertEquals(driverRideDTO1.getDriver(), result);
		} else {
			assertEquals(driverRideDTO2.getDriver(), result);
		}
	}

	static List<Arguments> fastestUnavailableSource() {
		return Arrays.asList(
				//           duration1, duration2, estimation1, estimation2, should win
				Arguments.of(10, 20, 10, 20, 1),
				Arguments.of(20, 10, 10, 20, 2),
				Arguments.of(10, 20, 20, 10, 1),
				Arguments.of(20, 10, 20, 10, 1),
				Arguments.of(40, 10, 10, 20, 2)
		);
	}



	@ParameterizedTest(name = "Computing driver priority")
	@MethodSource("unavailableTimesProvider")
	@DisplayName("Computing time to new ride destination")
	public void Unavailable_driver_takes_30_minutes_to_new_ride(LocalDateTime start, LocalDateTime now, int duration, int estimated, int expected) {
		// given
		Driver driver = new Driver();
		driver.setVehicle(new Vehicle());
		driver.getVehicle().setLatitude(1.0);
		driver.getVehicle().setLongitude(1.0);

		Ride currentRide = new Ride();
		initRide(currentRide);
		currentRide.setStartTime(start);
		currentRide.setDuration(duration);

		Place currentPlace = new Place();
		currentPlace.setLatitude(2.0);
		currentPlace.setLongitude(2.0);
		currentRide.getPlaces().add(currentPlace);
		DriverRideDTO driverRideDTO = new DriverRideDTO(driver, currentRide);

		Ride ride = new Ride();
		initRide(ride);
		Place place1 = new Place();
		place1.setLatitude(2.0);
		place1.setLongitude(2.0);
		ride.setPlaces(Arrays.asList(place1));

		when(timeEstimator.estimateTime(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(estimated);

		// when
		double result = rideService.computeUnavailableDriverPriorityForNewRide(driverRideDTO, ride, now);

		// then
		assertEquals(expected, result, 0.0);
	}

	static List<Arguments> unavailableTimesProvider() {
		return Arrays.asList(
				Arguments.arguments(
						LocalDateTime.of(2023, 11, 16, 10, 30),
						LocalDateTime.of(2023, 11, 16, 11, 0),
						60 * 60,
						1,
						30 * 60 + 1),
				Arguments.arguments(
						LocalDateTime.of(2023, 11, 16, 10, 30),
						LocalDateTime.of(2023, 11, 16, 11, 0),
						10 * 60,
						20 * 60,
						0)
		);
	}

	@Test
	public void Decline_valid_ride_passenger() {
		// given
		Passenger passenger = new Passenger();
		UUID rideId = UUID.randomUUID();


		Ride ride = new Ride();
		initRide(ride);
		List<Passenger> passengers = new ArrayList<>();
		passengers.add(passenger);
		ride.setPassengers(passengers);
		List<PassengerStatus> passengerStatus = new ArrayList<>();
		passengerStatus.add(PassengerStatus.Waiting);
		ride.setPassengersReady(passengerStatus);
		ride.setRideStatus(RideStatus.WAITING_FOR_PAYMENT);
		Mockito.when(rideRepository.findById(rideId)).thenReturn(java.util.Optional.of(ride));

		// when
		rideService.declineRidePassenger(passenger, rideId);

		// then
		Mockito.verify(rideRepository).findById(rideId);
		Mockito.verify(rideRepository).save(ride);

		assertEquals(RideStatus.DENIED, ride.getRideStatus());
		assertEquals(PassengerStatus.Declined, ride.getPassengersReady().get(0));
	}

	@Test
	public void Decline_invalid_ride_passenger() {
		// given
		Passenger passenger = new Passenger();
		UUID rideId = UUID.randomUUID();

		Ride ride = new Ride();
		Mockito.when(rideRepository.findById(rideId)).thenReturn(java.util.Optional.empty());

		// when
		assertThrows(EndRideException.class, () -> rideService.declineRidePassenger(passenger, rideId));
	}
}
