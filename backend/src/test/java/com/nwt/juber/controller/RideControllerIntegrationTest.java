package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.request.AdditionalRideRequests;
import com.nwt.juber.dto.request.LoginRequest;
import com.nwt.juber.dto.request.ride.*;
import com.nwt.juber.dto.response.TokenResponse;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.User;
import com.nwt.juber.model.VehicleType;
import com.nwt.juber.repository.Constants;
import com.nwt.juber.repository.RideRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RideControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RideRepository rideRepository;

	private final String password = "cascaded";

	private String token;

	HttpHeaders headers = new HttpHeaders();

	public void login(String username) {
		LoginRequest request = new LoginRequest();
		request.setEmail(username);
		request.setPassword(password);
		ResponseEntity<TokenResponse> tokenResp = restTemplate
				.exchange("/auth/login", HttpMethod.POST, new HttpEntity<>(request), TokenResponse.class);
		token = tokenResp.getBody().getAccessToken();
		headers.setBearerAuth(token);
	}

	public void activate(String driverEmail) {
		login(driverEmail);

		HttpEntity<String> entity = new HttpEntity<>(driverEmail, headers);
		ResponseEntity<String> response = restTemplate.exchange("/accounts/drivers/activate", HttpMethod.PATCH, entity, String.class);

		System.out.println(response.getBody());
	}

	@AfterEach
	public void cleanUp() {
		token = "";
		headers.clear();
	}

	@Test
	public void health() {
		login("petar.petrovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/health", HttpMethod.GET,  entity,String.class);
		System.out.println(responseEntity.getBody());
		System.out.println(responseEntity);
	}

	@ParameterizedTest
	@ValueSource(strings = {"8107614c-04d9-480d-8a59-e1999d9e7bfc", "46ab4aff-d171-4447-a05c-204a29d0fde1"})
	public void Ride_with_id(String id) {
		login("petar.petrovic@gmail.com");
		Ride ride = rideRepository.findById(UUID.fromString(id)).get();
		System.out.println(ride.getId());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/" + id, HttpMethod.GET,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
		assert responseEntity.getBody().getId().toString().equals(id);

	}


	@Test
	public void Active_ride_passenger() {
		login("petar.petrovic@gmail.com");

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/active", HttpMethod.GET,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
		assert responseEntity.getBody().getId().toString().equals("2ac4bc01-6326-418f-a3f9-4244e3922439");
	}

	@Test
	public void Start_ride_that_is_nonexistent() {
		headers = new HttpHeaders();
		String ride = "6aebc916-dd04-4674-a4f2-99edec0a1811";
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/start/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED;
	}

	@Test
	public void Start_valid_ride() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "2ac4bc01-6326-418f-a3f9-4244e3922439";
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/start/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
	}

	@Test
	public void Start_ride_that_is_not_accepted() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "3afa6238-862b-417b-9a88-fbf2bc90c09d";
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/start/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.NOT_ACCEPTABLE;
	}

	@Test
	public void End_nonexistent_ride() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "6aebc916-dd04-4674-a4f2-99edec0a1811";
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/end/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.NOT_ACCEPTABLE;
	}


	@Test
	public void End_ride_not_in_progress() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "7a1255b3-e69d-40f5-990d-bdfbe60e8258";
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/end/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.NOT_ACCEPTABLE;
	}


	@Test
	public void End_valid_ride() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "3afa6238-862b-417b-9a88-fbf2bc90c09d";
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/end/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
	}

	@Test
	public void End_valid_ride_unauthorized() {
		headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "3afa6238-862b-417b-9a88-fbf2bc90c09d";
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/end/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED;
	}

	@Test
	public void Panic_ride_unauthorized() {
		headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "3afa6238-862b-417b-9a88-fbf2bc90c09d";
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/panic/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED;
	}

	@Test
	public void Panic_ride_not_mine() {
		login("andrej.andrejevic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "3afa6238-862b-417b-9a88-fbf2bc90c09d";
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/panic/" + ride, HttpMethod.PUT,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.NOT_ACCEPTABLE;
	}

	@Test
	public void Panic_valid_ride() {
		login("mile.miletic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "3afa6238-862b-417b-9a88-fbf2bc90c09d";
		ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/panic/" + ride, HttpMethod.PUT,  entity,String.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
	}

	@Test
	public void Decline_ride_passenger() {
		login("branimir.branimirovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "412748b0-454f-4cac-8ab6-1388f9eebc19";
		ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/decline/" + ride, HttpMethod.PUT, entity, String.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
	}

	@Test
	public void Decline_ride_driver() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String ride = "412748b0-454f-4cac-8ab6-1388f9eebc19";
		ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/decline/" + ride, HttpMethod.PUT, entity, String.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
	}

	@ParameterizedTest
	@MethodSource("rideRequestProvider")
	public void Create_ride_request(RideRequestDTO requestDTO, HttpStatus status, String message) {
		activate("branko.brankovic@gmail.com");
		login("ivan.ivanovic@gmail.com");
		HttpEntity<RideRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/rideRequest", HttpMethod.POST, entity, String.class);

		System.out.println(responseEntity.getBody());

		assertTrue(responseEntity.hasBody());
		assertEquals(status, responseEntity.getStatusCode());
		if (!status.equals(HttpStatus.OK))
			assertTrue(responseEntity.getBody().contains(message));
	}

	@Test
	public void get_active_ride_driver() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);		
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/active", HttpMethod.GET, entity, RideDTO.class);
		assert responseEntity.getBody().getId().toString().equals(Constants.RIDE_3.toString());
		assert responseEntity.getStatusCode() == HttpStatus.OK;
	}
	
	@Test
	public void abandon_ride_invalid_passenger_for_ride() {
		login("petar.petrovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/abandon/"+ Constants.RIDE_3.toString(), HttpMethod.PUT, entity, String.class);
		assert responseEntity.getBody().contains("You are not allowed to abandon this ride!");
		assert responseEntity.getStatusCode() == HttpStatus.NOT_ACCEPTABLE;
	}
	@Test
	public void abandon_ride_passenger() {
		login("jovan.jovanovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/abandon/"+ Constants.RIDE_107.toString(), HttpMethod.PUT, entity, String.class);
		assert responseEntity.getBody().contains("Ride abandoned");
		assert responseEntity.getStatusCode() == HttpStatus.OK;
	}
	
	@Test
	public void abandon_ride_driver() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/abandon/"+ Constants.RIDE_3.toString(), HttpMethod.PUT, entity, String.class);
		assert responseEntity.getBody().contains("Ride abandoned");
		assert responseEntity.getStatusCode() == HttpStatus.OK;
	}
	
	@Test
	public void abandon_ride_invalid_driver_for_ride() {
		login("zdravko.zdravkovic@gmail.com");
		HttpEntity<String> entity = new HttpEntity<>(headers);		
		ResponseEntity<String> responseEntity = restTemplate.exchange("/ride/abandon/"+ Constants.RIDE_8.toString(), HttpMethod.PUT, entity, String.class);
		assert responseEntity.getBody().contains("You are not allowed to abandon this ride!");
		assert responseEntity.getStatusCode() == HttpStatus.NOT_ACCEPTABLE;
	}

	private static List<Arguments> rideRequestProvider() {
		List<RouteDTO> routes_1 = List.of(
				new RouteDTO(
						"Route 1",
						100.0,
						200.0,
						"test",
						true
				),
				new RouteDTO(
						"Route 2",
						100.0,
						200.0,
						"test",
						true
				)
		);

		List<PlaceDTO> places_1 = List.of(
				new PlaceDTO(
						null,
						"Place A",
						"Test",
						routes_1,
						10.0,
						10.0
				),
				new PlaceDTO(
						null,
						"Place B",
						"Test",
						routes_1,
						10.0,
						10.0
				),
				new PlaceDTO(
						null,
						"Place C",
						"Test",
						routes_1,
						10.0,
						10.0
				)
		);

		com.nwt.juber.dto.request.ride.RideDTO ride_1 = new com.nwt.juber.dto.request.ride.RideDTO(
				new ArrayList<>(),
				places_1,
				100.0,
				200,
				300.0
		);

		AdditionalRideRequestsDTO additionalRideRequests = new AdditionalRideRequestsDTO(
				false,
				false,
				new VehicleTypeDTO(
						UUID.fromString("920e64a8-50d7-42e0-90bd-714b48ab8e57"),
						"Estate",
						250.00
				)
		);

		RideRequestDTO rideRequestDTO_1 = new RideRequestDTO(
				ride_1,
				additionalRideRequests,
				"",
				new ArrayList<>()
		);

		return List.of(
			arguments(rideRequestDTO_1, HttpStatus.OK, "")
		);
	}

}
