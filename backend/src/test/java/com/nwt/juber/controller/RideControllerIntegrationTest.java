package com.nwt.juber.controller;

import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.request.LoginRequest;
import com.nwt.juber.dto.response.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:application-test.yml")
@Profile("test")
public class RideControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	private final String passengerUsername = "petar.petrovic@gmail.com";
	private final String passengerPassword = "cascaded";

	@Test
	public void login() {
		LoginRequest request = new LoginRequest();
		request.setEmail(passengerUsername);
		request.setPassword(passengerPassword);
		ResponseEntity<TokenResponse> token = restTemplate
				.exchange("/auth/login", HttpMethod.POST, new HttpEntity<>(request), TokenResponse.class);
		System.out.println(token);
		System.out.println(token.getStatusCode());
		System.out.println(token.getStatusCodeValue());
		System.out.println(token.getBody());
	}
}
