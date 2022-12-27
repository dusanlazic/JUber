package com.nwt.juber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nwt.juber.dto.request.AdditionalRideRequests;
import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideStatus;
import com.nwt.juber.repository.RideRepository;

@Service
public class RideService {

	@Autowired
	private RideRepository rideRepository;
	
	@Autowired
	private PassengerService passengerService;
	
	@Autowired
	private DriverService driverService;

	public Ride createRideRequest(RideRequest rideRequest) {
		Ride ride = new Ride();
		for (String passengerEmail : rideRequest.getPassengerEmails()) {
			Passenger passenger = passengerService.findByEmail(passengerEmail);
			ride.getPassengers().add(passenger);
		}
		ride.setRoute(rideRequest.route);
		ride.setDriver(null);
		ride.setStartTime(null);
		ride.setEndTime(null);
		ride.setRideStatus(RideStatus.WAIT);
		
		if(ride.getPassengers().size() > 1) {
			// sendAcceptRideNotifications(ride);
		}
		else {
			assignDriver(ride, rideRequest.getAdditionalRequests());
		}
		
		// calculatePrice(ride);
		
		ride = rideRepository.save(ride);
		return ride;
	}
	
	public Driver assignDriver(Ride ride, AdditionalRideRequests additionalRequsets) {
		Driver driver = driverService.findSuitableDriver(ride, additionalRequsets);
		if(driver != null) {
			// sendAcceptRideNotifications(ride);
			ride.setDriver(driver);
			ride.setRideStatus(RideStatus.ACCEPTED);
		}
		else {
			System.out.println("[!] Driver not found!");
		}
		return driver;
	}
	
	}
