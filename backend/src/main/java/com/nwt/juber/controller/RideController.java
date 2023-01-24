package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.response.report.ReportResponse;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.service.RideService;

import java.util.*;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.InsufficientResourcesException;
import java.util.UUID;

import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.dto.response.PastRidesResponse;
import com.nwt.juber.model.User;

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

    @PutMapping("/accept/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ResponseOk acceptRide(@PathVariable("id") UUID rideId, Authentication authentication) throws InsufficientResourcesException {
        rideService.acceptRide(rideId, authentication);
        return new ResponseOk("ok");
    }

    @PutMapping("/decline/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ResponseOk declineRide(@PathVariable("id") UUID rideId, Authentication authentication) throws InsufficientResourcesException {
        rideService.declineRide(rideId, authentication);
        return new ResponseOk("ok");
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER', 'ADMIN')")
    public RideDTO getRide(@PathVariable("id") UUID rideId, Authentication authentication) {
        return rideService.getRide(rideId, authentication);
    }

    @PutMapping("favourite/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseOk toggleFavouriteRide(@PathVariable("id") UUID rideId, Authentication authentication) {
        System.out.println("Toggling favourite...");
        rideService.toggleFavourite(rideId, authentication);
        return new ResponseOk("ok");
    }

    @GetMapping("is-favourite/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public CheckFavouriteResponse checkFavouriteRide(@PathVariable("id") UUID rideId, Authentication authentication) {
        return new CheckFavouriteResponse(rideService.checkIfFavourite(rideId, authentication));
    }

    @Data
    @AllArgsConstructor
    class CheckFavouriteResponse {
        boolean isFavourite;
    }



    @GetMapping("/active")
    @PreAuthorize(("hasAnyRole('DRIVER', 'PASSENGER')"))
    public RideDTO getActiveRide(Authentication authentication) {
        RideDTO rideDTO = rideService.getActiveRide(authentication);
        return rideDTO;
    }
    
    @PostMapping("/rideRequest")
	@PreAuthorize("hasAnyRole('PASSENGER')")
	public ResponseOk createRideRequest(@Valid @RequestBody RideRequest rideRequest, Authentication authentication) {
		System.out.println(rideRequest.toString());
		// TODO: rideService.createRideRequest(rideRequest);
        rideService.createRideRequest(rideRequest, authentication);
        return new ResponseOk("ok");
	}


    @PutMapping("/abandon/{id}")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseOk abandonRide(@PathVariable("id") UUID rideId, @RequestBody String reason, Authentication authentication) {
        rideService.abandonRide(rideId, reason, authentication);
        return new ResponseOk("ok");
    }


    @GetMapping("/pastRides")
    @PreAuthorize(("hasAnyRole('DRIVER', 'PASSENGER')"))
    public List<PastRidesResponse> getPastRides(Authentication authentication) {
    	User user = (User) authentication.getPrincipal();
    	return rideService.getPastRides(user);
    }

    @GetMapping("/savedRoutes")
    @PreAuthorize("hasRole('PASSENGER')")
    public List<RideDTO> getSavedRoutes(Authentication authentication) {
        Passenger passenger = (Passenger) authentication.getPrincipal();
        return rideService.getSavedRoutes(passenger);
    }

}
