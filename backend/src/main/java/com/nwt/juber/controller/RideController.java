package com.nwt.juber.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.service.RideService;

@RestController
@RequestMapping(value = "ride")
public class RideController {

    @Autowired
    RideService rideService;

    @PutMapping("/start/{id}")
    public ResponseOk startRide(@PathVariable("id") UUID rideId) {
        rideService.startRide(rideId);
        return new ResponseOk("ok");
    }

    @PutMapping("/end/{id}")
    public ResponseOk endRide(@PathVariable("id") UUID rideId) {
        rideService.endRide(rideId);
        return new ResponseOk("ok");
    }
    
    @PostMapping("/rideRequest")
	@PreAuthorize("hasAnyRole('PASSENGER')")
	public void createRideRequest(@Valid @RequestBody RideRequest rideRequest) {
		System.out.println(rideRequest.toString());
		// TODO: rideService.createRideRequest(rideRequest);
	}
}
