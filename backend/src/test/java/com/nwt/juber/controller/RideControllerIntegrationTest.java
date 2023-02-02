package com.nwt.juber.controller;

import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.request.LoginRequest;
import com.nwt.juber.dto.response.TokenResponse;
import com.nwt.juber.model.Ride;
import com.nwt.juber.repository.RideRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;


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
	public void Active_ride() {
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


}
