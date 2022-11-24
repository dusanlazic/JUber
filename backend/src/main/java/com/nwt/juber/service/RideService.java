package com.nwt.juber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.repository.RideRepository;

@Service
public class RideService {

	@Autowired
	private RideRepository rideRepository;

	public void createRideRequest(RideRequest rideRequest) {
		System.out.println("[!] createRideRequest() METHOD NOT IMPLEMENTED");
	}
}
