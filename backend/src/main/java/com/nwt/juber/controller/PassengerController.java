package com.nwt.juber.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.juber.dto.PersonDTO;
import com.nwt.juber.dto.response.PastRidesResponse;
import com.nwt.juber.dto.response.UserBasicInfoResponse;
import com.nwt.juber.service.PassengerService;
import com.nwt.juber.service.RideService;


@RestController
@RequestMapping(value = "/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerController {

	@Autowired
	private PassengerService passengerService;

	@Autowired
	private RideService rideService;

	@GetMapping("/basicInfo/{email}")
	public UserBasicInfoResponse getBasicInfo(@PathVariable String email) {
		return passengerService.getBasicInfo(email);
	}

	@GetMapping("/{passengerId}/info")
	@PreAuthorize("hasRole('ADMIN')")
	public PersonDTO getDriversInfo(@PathVariable UUID passengerId) {
		return passengerService.getPassengersInfo(passengerId);
	}
	
	@GetMapping("/findAll")
	@PreAuthorize("hasRole('ADMIN')")
	public List<PersonDTO> findAll() {
		return passengerService.findAll();
	}

	@GetMapping("/{passengerId}/rides")
	@PreAuthorize("hasRole('ADMIN')")
	public List<PastRidesResponse> getPassengersPastRides(@PathVariable UUID passengerId) {
		return rideService.getPastRides(passengerId);
	}
}
