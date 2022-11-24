package com.nwt.juber.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.service.RideService;

@RestController
@RequestMapping(value = "/ride", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideController {

	@Autowired
	private RideService rideService;

	@PostMapping("/rideRequest")
	@PreAuthorize("hasAnyRole('PASSENGER')")
	public void createRideRequest(@Valid @RequestBody RideRequest rideRequest) {
		System.out.println(rideRequest.toString());
		rideService.createRideRequest(rideRequest);
	}
}
