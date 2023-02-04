package com.nwt.juber.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nwt.juber.dto.DriverRideDTO;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.exception.AbandonRideException;
import com.nwt.juber.exception.DriverNotFoundException;
import com.nwt.juber.exception.EndRideException;
import com.nwt.juber.exception.InsufficientFundsException;
import com.nwt.juber.exception.StartRideException;
import com.nwt.juber.exception.UserAlreadyInRideException;
import com.nwt.juber.model.Admin;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.PassengerStatus;
import com.nwt.juber.model.Place;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideCancellation;
import com.nwt.juber.model.RideStatus;
import com.nwt.juber.model.Role;
import com.nwt.juber.model.User;
import com.nwt.juber.model.Vehicle;
import com.nwt.juber.model.VehicleType;
import com.nwt.juber.model.notification.NewRideAssignedNotification;
import com.nwt.juber.model.notification.RideStatusUpdatedNotification;
import com.nwt.juber.repository.DriverRepository;
import com.nwt.juber.repository.PassengerRepository;
import com.nwt.juber.repository.PlaceRepository;
import com.nwt.juber.repository.RideCancellationRepository;
import com.nwt.juber.repository.RideRepository;
import com.nwt.juber.repository.RouteRepository;
import com.nwt.juber.repository.VehicleTypeRepository;

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

	@Mock
	private PassengerRepository passengerRepository;

	@Mock
	private RouteRepository routeRepository;

	@Mock
	private PlaceRepository placeRepository;

	@Mock
	private NotificationService notificationService;

	@Mock
	private TaskScheduling taskScheduling;

	@Mock
	private RideCancellationRepository rideCancellationRepository;

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

		when(driverRepository.findAvailableDrivers()).thenReturn(drivers);

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

		when(driverRepository.findUnavailableDriversWithNoFutureRides()).thenReturn(drivers);
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

	@Test
	public void Decline_passenger_that_is_already_in_ride() {
		// given
		Passenger passenger = new Passenger();
		Mockito.when(rideRepository.getActiveRideForPassenger(passenger)).thenReturn(new Ride());

		// when
		assertThrows(UserAlreadyInRideException.class, () ->
				rideService.createRideRequest(new RideRequest(), new Passenger()));
	}

	@ParameterizedTest
	@MethodSource("insufficientFundsProvider")
	public void Decline_passenger_that_has_insufficient_funds(BigDecimal passengerFunds, double rideFare, int numOfPeople) {
		// given
		Passenger passenger = new Passenger();
		passenger.setBalance(passengerFunds);
		Ride ride = new Ride();
		ride.setFare(rideFare);
		ride.setPassengers(new ArrayList<>());
		numOfPeople--;
		while (numOfPeople > 0) {
			ride.getPassengers().add(new Passenger());
			numOfPeople--;
		}
		RideRequest rideRequest = new RideRequest();
		rideRequest.setRide(ride);

		// when
		assertThrows(InsufficientFundsException.class, () ->
				rideService.createRideRequest(rideRequest, passenger));
	}

	static List<Arguments> insufficientFundsProvider() {
		return Arrays.asList(
				Arguments.arguments(BigDecimal.ZERO, 100.0, 1),
				Arguments.arguments(BigDecimal.valueOf(99), 100.0, 1),
				Arguments.arguments(BigDecimal.valueOf(99), 500.0, 5),
				Arguments.arguments(BigDecimal.valueOf(33), 100.0, 3)
		);
	}

	@Test
	public void Set_schedule_task_if_ride_scheduled() {
		// given
		Driver driver = new Driver();
		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		ride.setDriver(driver);
		ride.setScheduledTime(LocalDateTime.now().plusHours(1));
		ride.setPassengers(new ArrayList<>());
		ride.setPlaces(new ArrayList<>());

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		rideService.acceptRideDriver(driver, rideId);

		// then
		assertEquals(RideStatus.SCHEDULED, ride.getRideStatus());
	}

	@Test
	public void Accept_ride_driver_if_ride_not_scheduled_and_active_ride_in_progress() {
		// given
		Driver driver = new Driver();
		UUID driverId = UUID.randomUUID();
		driver.setId(driverId);
		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		ride.setDriver(driver);
		ride.setPassengers(new ArrayList<>());
		ride.setPlaces(new ArrayList<>());

		Ride activeRide = new Ride();
		activeRide.setRideStatus(RideStatus.ACCEPTED);
		activeRide.setDuration(100);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
		Mockito.when(rideRepository.getActiveRideForDriver(driverId)).thenReturn(activeRide);

		// when
		rideService.acceptRideDriver(driver, rideId);

		// then
		assertEquals(RideStatus.SCHEDULED, ride.getRideStatus());
	}

	@Test
	public void Accept_ride_driver_if_ride_not_scheduled() {
		// given
		Driver driver = new Driver();
		UUID driverId = UUID.randomUUID();
		driver.setId(driverId);
		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		ride.setDriver(driver);
		ride.setPassengers(new ArrayList<>());
		ride.setPlaces(new ArrayList<>());

		Ride activeRide = new Ride();
		activeRide.setRideStatus(RideStatus.FINISHED);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
		Mockito.when(rideRepository.getActiveRideForDriver(driverId)).thenReturn(activeRide);

		// when
		rideService.acceptRideDriver(driver, rideId);

		// then
		assertEquals(RideStatus.ACCEPTED, ride.getRideStatus());
	}

	@Test
	public void Decline_ride_as_driver() throws DriverNotFoundException {
		// given
		Driver driver = new Driver();
		Ride ride = new Ride();
		initRide(ride);
		UUID rideId = UUID.randomUUID();
		ride.setId(rideId);
		Place place1 = new Place();
		place1.setLatitude(2.0);
		place1.setLongitude(2.0);
		ride.setPlaces(List.of(place1));
		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		rideService.declineRideDriver(driver, rideId);

		// then
		verify(rideRepository, atLeastOnce()).save(ride);
		assertTrue(ride.getBlacklisted().contains(driver));
	}

	@Test
	public void Get_ride_as_authorized_passenger() {
		// given
		Driver driver = new Driver();
		driver.setId(UUID.randomUUID());
		driver.setRole(Role.ROLE_DRIVER);

		Passenger passenger = new Passenger();
		passenger.setId(UUID.randomUUID());
		passenger.setRole(Role.ROLE_PASSENGER);

		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		initRide(ride);
		ride.setId(rideId);
		ride.setDriver(driver);
		ride.getPassengers().add(passenger);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		RideDTO rideDTO = rideService.getRide(rideId, passenger);

		// then
		assertTrue(rideDTO.getPassengers().stream().anyMatch(o -> o.getId().equals(passenger.getId())));
	}

	@Test
	public void Get_ride_as_authorized_driver() {
		// given
		Driver driver = new Driver();
		driver.setId(UUID.randomUUID());
		driver.setRole(Role.ROLE_DRIVER);

		Passenger passenger = new Passenger();
		passenger.setId(UUID.randomUUID());
		passenger.setRole(Role.ROLE_PASSENGER);

		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		initRide(ride);
		ride.setId(rideId);
		ride.setDriver(driver);
		ride.getPassengers().add(passenger);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		RideDTO rideDTO = rideService.getRide(rideId, driver);

		// then
		assertEquals(rideDTO.getDriver().getId(), driver.getId());
	}

	@Test
	public void Get_ride_as_unauthorized_user() {
		// given
		Driver driver = new Driver();
		driver.setId(UUID.randomUUID());
		driver.setRole(Role.ROLE_DRIVER);

		Passenger passenger = new Passenger();
		passenger.setId(UUID.randomUUID());
		passenger.setRole(Role.ROLE_PASSENGER);

		Passenger unauthorizedPassenger = new Passenger();
		unauthorizedPassenger.setId(UUID.randomUUID());
		unauthorizedPassenger.setRole(Role.ROLE_PASSENGER);

		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		initRide(ride);
		ride.setId(rideId);
		ride.setDriver(driver);
		ride.getPassengers().add(passenger);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// then
		assertThrows(RuntimeException.class, () -> rideService.getRide(rideId, unauthorizedPassenger));
	}

	@Test
	public void Get_ride_as_admin() {
		// given
		Driver driver = new Driver();
		driver.setId(UUID.randomUUID());
		driver.setRole(Role.ROLE_DRIVER);

		Passenger passenger = new Passenger();
		passenger.setId(UUID.randomUUID());
		passenger.setRole(Role.ROLE_PASSENGER);

		Admin admin = new Admin();
		admin.setId(UUID.randomUUID());
		admin.setRole(Role.ROLE_ADMIN);

		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		initRide(ride);
		ride.setId(rideId);
		ride.setDriver(driver);
		ride.getPassengers().add(passenger);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// then
		assertDoesNotThrow(() -> rideService.getRide(rideId, admin));
	}

	@Test
	public void Abandon_ride_as_unauthorized_driver() {
		// given
		Driver driver = new Driver();
		driver.setId(UUID.randomUUID());

		Driver unauthorizedDriver = new Driver();
		unauthorizedDriver.setId(UUID.randomUUID());

		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		initRide(ride);
		ride.setId(rideId);
		ride.setDriver(driver);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		AbandonRideException exception = assertThrows(AbandonRideException.class, () -> rideService.abandonRideDriver(rideId, "Whatever", unauthorizedDriver));
		assertEquals("You are not allowed to abandon this ride!", exception.getMessage());
		verify(rideRepository, never()).save(ride);
	}

	@Test
	public void Abandon_ride_as_driver() {
		// given
		Driver driver = new Driver();
		UUID driverId = UUID.randomUUID();
		driver.setId(driverId);

		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		initRide(ride);
		ride.setId(rideId);
		ride.setDriver(driver);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		rideService.abandonRideDriver(rideId, "Whatever", driver);

		// then
		assertEquals(RideStatus.DENIED, ride.getRideStatus());
		verify(rideRepository).save(ride);
	}

	@Test
	public void Start_scheduled_ride() {
		// given
		Driver driver = new Driver();
		UUID driverId = UUID.randomUUID();
		driver.setId(driverId);
		
		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		ride.setDriver(driver);
		ride.setId(rideId);
		ride.setScheduledTime(LocalDateTime.now().minusSeconds(3));;

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
		Mockito.when(rideRepository.getActiveRideForDriver(driverId)).thenReturn(null);

		// when
		rideService.startScheduledRide(rideId);
            
		// then
		verify(rideRepository, times(1)).save(any(Ride.class));
		verify(notificationService, times(1)).send(any(RideStatusUpdatedNotification.class), any(Driver.class));
	}
	
	@Test
	public void Abandon_ride_as_unauthorized_passenger() {
		// given
		Passenger passenger = new Passenger();
		UUID passengerId = UUID.randomUUID();
		passenger.setId(passengerId);
		
		Passenger unauthorizedPassenger = new Passenger();
		UUID unauthorizedPassengerId = UUID.randomUUID();
		passenger.setId(unauthorizedPassengerId);

		Ride ride = new Ride();
		initRide(ride);
		UUID rideId = UUID.randomUUID();
		ride.setId(rideId);
		ride.getPassengers().add(passenger);
		
		String reason = "Whatever reason";
		
		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		AbandonRideException exception = assertThrows(AbandonRideException.class, () -> rideService.abandonRidePassenger(rideId, reason, unauthorizedPassenger));
		
		//then
		assertEquals("You are not allowed to abandon this ride!", exception.getMessage());
		verify(rideRepository, never()).save(ride);
	}
	
	@Test
	public void Abandon_ride_as_passenger() {
		// given
		Passenger passenger = new Passenger();
		UUID passengerId = UUID.randomUUID();
		passenger.setId(passengerId);

		Ride ride = new Ride();
		initRide(ride);
		UUID rideId = UUID.randomUUID();
		ride.setRideStatus(RideStatus.WAITING_FOR_PAYMENT);
		ride.setId(rideId);
		ride.getPassengers().add(passenger);
		
		String reason = "Whatever reason";

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		rideService.abandonRidePassenger(rideId, reason, passenger);

		// then
		assertEquals(RideStatus.DENIED, ride.getRideStatus());
		verify(rideRepository).save(ride);
		verify(rideCancellationRepository, times(1)).save(any(RideCancellation.class));

	}
	@Test
	public void accept_ride_passenger_ride_not_found() throws InsufficientFundsException, DriverNotFoundException {
		// given
		Passenger passenger = new Passenger();
		UUID rideId = UUID.randomUUID();

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.empty());

		// when
		EndRideException exception = assertThrows(EndRideException.class, () -> rideService.acceptRidePassenger(passenger, rideId));
		
		//then
		assertEquals("No ride with id: "+rideId, exception.getMessage());
		verify(rideRepository, never()).save(any(Ride.class));
	}
	@Test
	public void accept_ride_passenger_ride_invalid_status() throws InsufficientFundsException, DriverNotFoundException {
		// given
		Passenger passenger = new Passenger();
		
		Ride ride = new Ride();
		UUID rideId = UUID.randomUUID();
		ride.setRideStatus(RideStatus.IN_PROGRESS);
		ride.setId(rideId);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		EndRideException exception = assertThrows(EndRideException.class, () -> rideService.acceptRidePassenger(passenger, rideId));
		
		//then
		assertEquals("Ride is not waiting for payment!", exception.getMessage());
		verify(rideRepository, never()).save(any(Ride.class));
	}
	@Test
	public void accept_ride_passenger_ride_Insufficient_Funds() throws InsufficientFundsException, DriverNotFoundException {
		// given
		Passenger passenger = new Passenger();
		passenger.setBalance(new BigDecimal(1));

		Ride ride = new Ride();
		ride.setFare((double) 10000);
		initRide(ride);	
		UUID rideId = UUID.randomUUID();
		ride.setRideStatus(RideStatus.WAITING_FOR_PAYMENT);
		ride.setId(rideId);
		ride.getPassengers().add(passenger);
		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> rideService.acceptRidePassenger(passenger, rideId));
		
		//then
		assertEquals("Not enough funds!", exception.getMessage());
		verify(rideRepository, never()).save(any(Ride.class));
	}
	@Test
	public void accept_ride_not_all_passengers_ready() throws InsufficientFundsException, DriverNotFoundException {
		// given
		Passenger passenger = new Passenger();
		passenger.setBalance(new BigDecimal(1000));
		Passenger passenger2 = new Passenger();

		Ride ride = new Ride();
		ride.setFare((double) 1);
		initRide(ride);	
		UUID rideId = UUID.randomUUID();
		ride.setRideStatus(RideStatus.WAITING_FOR_PAYMENT);
		ride.setId(rideId);
		ride.getPassengersReady().add(PassengerStatus.Waiting);
		ride.getPassengersReady().add(PassengerStatus.Waiting);
		ride.getPassengers().add(passenger);
		ride.getPassengers().add(passenger2);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		rideService.acceptRidePassenger(passenger, rideId);
		
		//then
		verify(rideRepository, never()).save(any(Ride.class));
		assertEquals(ride.getPassengersReady().get(0), PassengerStatus.Ready);
	}
	
	@Test
	public void accept_ride_all_passengers_no_driver() throws InsufficientFundsException, DriverNotFoundException {
		// given
		Passenger passenger = new Passenger();
		passenger.setBalance(new BigDecimal(1000));

		Ride ride = new Ride();
		ride.setFare((double) 1);
		initRide(ride);	
		UUID rideId = UUID.randomUUID();
		ride.setRideStatus(RideStatus.WAITING_FOR_PAYMENT);
		ride.setId(rideId);
		ride.getPassengersReady().add(PassengerStatus.Waiting);
		ride.getPassengers().add(passenger);
		List<Place> places1 = new ArrayList<>();
		Place place1 = new Place();
		place1.setLatitude(1.0);
		place1.setLongitude(1.0);
		places1.add(place1);
		Place place2 = new Place();
		place2.setLatitude(2.0);
		place2.setLongitude(2.0);
		places1.add(place2);

		ride.setPlaces(places1);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));

		// when
		DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> rideService.acceptRidePassenger(passenger, rideId));
		
		//then
		verify(rideRepository, times(2)).save(any(Ride.class));
		assertEquals("No driver found!", exception.getMessage());
	}
	
	@Test
	public void accept_ride_all_passengers_assign_driver() throws InsufficientFundsException, DriverNotFoundException {
		// given
		Passenger passenger = new Passenger();
		passenger.setBalance(new BigDecimal(1000));

		Place startPlace = new Place();
		startPlace.setLatitude(1.0);
		startPlace.setLongitude(1.0);
		List<Place> places = new ArrayList<>();
		places.add(startPlace);

		Ride ride = new Ride();
		initRide(ride);	
		UUID rideId = UUID.randomUUID();
		ride.setId(rideId);

		ride.setFare((double) 1);
		ride.setRideStatus(RideStatus.WAITING_FOR_PAYMENT);
		ride.getPassengersReady().add(PassengerStatus.Waiting);
		ride.getPassengers().add(passenger);
		ride.setPlaces(places);

		VehicleType vehicleType = new VehicleType();
		ride.setVehicleTypeRequested(vehicleType);

		Driver driver1 = new Driver();
		driver1.setVersion(1);
		driver1.setBlocked(false);
		Vehicle vehicle1 = new Vehicle();
		vehicle1.setCapacity(4);
		vehicle1.setVehicleType(vehicleType);
		vehicle1.setLatitude(2.0);
		vehicle1.setLongitude(2.0);
		
		driver1.setVehicle(vehicle1);

		List<Driver> drivers = new ArrayList<>();
		drivers.add(driver1);

		Mockito.when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
		when(driverRepository.findAvailableDrivers()).thenReturn(drivers);

		// when
		rideService.acceptRidePassenger(passenger, rideId);
		
		//then
		verify(rideRepository, times(2)).save(any(Ride.class));
		verify(driverRepository, times(1)).save(any(Driver.class));
		verify(notificationService, times(1)).send(any(NewRideAssignedNotification.class), any(Driver.class));
	}
	
	@Test
	public void get_active_ride_passenger() {
		// given
		User user = new Passenger();
		UUID userId = UUID.randomUUID();
		user.setId(userId);
		user.setRole(Role.ROLE_PASSENGER);
		
		Passenger passenger = (Passenger) user;
		Ride ride = new Ride();
		
		RideDTO expectedDTO = new RideDTO();
		expectedDTO.setPlaces(new ArrayList<>());
		expectedDTO.setPassengers(new ArrayList<>());
		expectedDTO.setPassengersReady(new ArrayList<>());
		initRide(ride);	

		Mockito.when(passengerRepository.findById(userId)).thenReturn(Optional.of(passenger));
		Mockito.when(rideRepository.getActiveRideForPassenger(passenger)).thenReturn(ride);
		
		// when
		RideDTO dto = rideService.getActiveRide(user);
		
		//then
		verify(rideRepository, times(1)).getActiveRideForPassenger(passenger);
		verify(passengerRepository, times(1)).findById(userId);
		assertEquals(dto.getDriver(), expectedDTO.getDriver());
		assertEquals(dto.getPlaces(), expectedDTO.getPlaces());
		assertEquals(dto.getFare(), expectedDTO.getFare());
		assertEquals(dto.getPassengers(), expectedDTO.getPassengers());
		assertEquals(dto.getPassengersReady(), expectedDTO.getPassengersReady());
		assertEquals(dto.getRideStatus(), expectedDTO.getRideStatus());
		assertEquals(dto.getBabyFriendly(), expectedDTO.getBabyFriendly());
		assertEquals(dto.getPetFriendly(), expectedDTO.getPetFriendly());
		assertEquals(dto.getScheduledTime(), expectedDTO.getScheduledTime());
	}
	@Test
	public void get_active_ride_driver() {
		// given
		User user = new Driver();
		UUID userId = UUID.randomUUID();
		user.setId(userId);
		user.setRole(Role.ROLE_DRIVER);
		
		Driver driver = (Driver) user;
		Ride ride = new Ride();
		
		RideDTO expectedDTO = new RideDTO();
		expectedDTO.setPlaces(new ArrayList<>());
		expectedDTO.setPassengers(new ArrayList<>());
		expectedDTO.setPassengersReady(new ArrayList<>());
		initRide(ride);	

		Mockito.when(driverRepository.findById(userId)).thenReturn(Optional.of(driver));
		Mockito.when(rideRepository.getActiveRideForDriver(userId)).thenReturn(ride);
		
		// when
		RideDTO dto = rideService.getActiveRide(user);
		
		//then
		verify(rideRepository, times(1)).getActiveRideForDriver(userId);
		verify(driverRepository, times(1)).findById(userId);
		assertEquals(dto.getDriver(), expectedDTO.getDriver());
		assertEquals(dto.getPlaces(), expectedDTO.getPlaces());
		assertEquals(dto.getFare(), expectedDTO.getFare());
		assertEquals(dto.getPassengers(), expectedDTO.getPassengers());
		assertEquals(dto.getPassengersReady(), expectedDTO.getPassengersReady());
		assertEquals(dto.getRideStatus(), expectedDTO.getRideStatus());
		assertEquals(dto.getBabyFriendly(), expectedDTO.getBabyFriendly());
		assertEquals(dto.getPetFriendly(), expectedDTO.getPetFriendly());
		assertEquals(dto.getScheduledTime(), expectedDTO.getScheduledTime());
	}

}
