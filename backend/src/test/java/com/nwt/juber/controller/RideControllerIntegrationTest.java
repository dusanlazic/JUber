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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RideControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RideRepository rideRepository;

	private final String passengerUsername = "petar.petrovic@gmail.com";
	private final String passengerPassword = "cascaded";

	private String passengerToken;

	HttpHeaders passengerHeaders = new HttpHeaders();

	public void login() {
		LoginRequest request = new LoginRequest();
		request.setEmail(passengerUsername);
		request.setPassword(passengerPassword);
		ResponseEntity<TokenResponse> token = restTemplate
				.exchange("/auth/login", HttpMethod.POST, new HttpEntity<>(request), TokenResponse.class);
		passengerToken = token.getBody().getAccessToken();
		passengerHeaders.setBearerAuth(passengerToken);
	}

	@Test
	public void health() {
		login();
		HttpEntity<String> entity = new HttpEntity<>(passengerHeaders);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/health", HttpMethod.GET,  entity,String.class);
		System.out.println(responseEntity.getBody());
		System.out.println(responseEntity);
	}

	@ParameterizedTest
	@ValueSource(strings = {"8107614c-04d9-480d-8a59-e1999d9e7bfc", "46ab4aff-d171-4447-a05c-204a29d0fde1"})
	public void Ride_with_id(String id) {
		login();
		Ride ride = rideRepository.findById(UUID.fromString(id)).get();
		System.out.println(ride.getId());
		HttpEntity<String> entity = new HttpEntity<>(passengerHeaders);
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/" + id, HttpMethod.GET,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
		assert responseEntity.getBody().getId().toString().equals(id);

	}


	@Test
	public void Active_ride() {
		login();
		HttpEntity<String> entity = new HttpEntity<>(passengerHeaders);
		ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/ride/active", HttpMethod.GET,  entity,RideDTO.class);
		assert responseEntity.getBody() != null;
		assert responseEntity.getStatusCode() == HttpStatus.OK;
		assert responseEntity.getBody().getId().toString().equals("2ac4bc01-6326-418f-a3f9-4244e3922439");
	}


}
