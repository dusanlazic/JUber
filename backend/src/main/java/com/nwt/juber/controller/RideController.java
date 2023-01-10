package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.InsufficientResourcesException;
import java.util.UUID;

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
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseOk acceptRide(@PathVariable("id") UUID rideId, Authentication authentication) throws InsufficientResourcesException {
        rideService.acceptRide(rideId, authentication);
        return new ResponseOk("ok");
    }

    @PutMapping("/decline/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseOk declineRide(@PathVariable("id") UUID rideId, Authentication authentication) throws InsufficientResourcesException {
        rideService.declineRide(rideId, authentication);
        return new ResponseOk("ok");
    }

    @GetMapping("/active")
    @PreAuthorize(("hasAnyRole('DRIVER', 'PASSENGER')"))
    public RideDTO getActiveRide(Authentication authentication) {
        RideDTO rideDTO = rideService.getActiveRide(authentication);
        return rideDTO;
    }
}
