package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.request.ride.RideRequestDTO;
import com.nwt.juber.dto.response.PastRidesResponse;
import com.nwt.juber.exception.DriverNotFoundException;
import com.nwt.juber.exception.InsufficientFundsException;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.User;
import com.nwt.juber.service.DriverService;
import com.nwt.juber.service.PassengerService;
import com.nwt.juber.service.RideService;
import com.nwt.juber.util.MappingUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.InsufficientResourcesException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "ride")
@Transactional
public class RideController {

    @Autowired
    RideService rideService;

    @Autowired
    PassengerService passengerService;

    @Autowired
    DriverService driverService;

    @PutMapping("/start/{id}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseOk startRide(@PathVariable("id") UUID rideId) {
        rideService.startRide(rideId);
        return new ResponseOk("ok");
    }

    @PutMapping("/end/{id}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseOk endRide(@PathVariable("id") UUID rideId, Authentication authentication) {
        Driver driver = driverService.findById(((User) authentication.getPrincipal()).getId()).orElseThrow(UserNotFoundException::new);
        rideService.endRide(rideId);
        return new ResponseOk("ok");
    }


    @PutMapping("/accept/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ResponseOk acceptRide(@PathVariable("id") UUID rideId, Authentication authentication) throws InsufficientFundsException, DriverNotFoundException {
        User user = (User) authentication.getPrincipal();
        Optional<Passenger> passenger = passengerService.findById(user.getId());
        if (passenger.isPresent()) {
            rideService.acceptRidePassenger(passenger.get(), rideId);
        } else {
            Driver driver = driverService.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("No driver found!"));
            rideService.acceptRideDriver(driver, rideId);
        }
        return new ResponseOk("ok");
    }

    @PutMapping("/panic/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseOk panicRide(@PathVariable("id") UUID rideId, Authentication authentication) {
        Passenger passenger = passengerService.findById(((User) authentication.getPrincipal()).getId()).orElseThrow();
        Ride ride = rideService.findRideById(rideId).orElseThrow(() -> new RuntimeException("No ride found!"));
        rideService.panicRide(ride, passenger);
        return new ResponseOk("ok");
    }

    @PutMapping("/decline/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public ResponseOk declineRide(@PathVariable("id") UUID rideId, Authentication authentication) throws InsufficientResourcesException, DriverNotFoundException {
        User user = (User) authentication.getPrincipal();
        Optional<Passenger> passenger = passengerService.findById(user.getId());
        if(passenger.isPresent()) {
            rideService.declineRidePassenger(passenger.get(), rideId);
        } else {
            Driver driver = driverService.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("No driver found!"));
            rideService.declineRideDriver(driver, rideId);
        }
        return new ResponseOk("ok");
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER', 'ADMIN')")
    public RideDTO getRide(@PathVariable("id") UUID rideId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return rideService.getRide(rideId, user);
    }

    @PutMapping("favourite/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseOk toggleFavouriteRide(@PathVariable("id") UUID rideId, Authentication authentication) {
        System.out.println("Toggling favourite...");
        Passenger passenger = passengerService.findById(((User) authentication.getPrincipal()).getId()).orElseThrow();
        rideService.toggleFavourite(rideId, passenger);
        return new ResponseOk("ok");
    }

    @GetMapping("is-favourite/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public CheckFavouriteResponse checkFavouriteRide(@PathVariable("id") UUID rideId, Authentication authentication) {
        Passenger passenger = passengerService.findById(((User) authentication.getPrincipal()).getId()).orElseThrow();
        return new CheckFavouriteResponse(rideService.checkIfFavourite(rideId, passenger));
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
	public ResponseOk createRideRequest(@Valid @RequestBody RideRequestDTO rideRequest, Authentication authentication) throws DriverNotFoundException {
        User user = (User) authentication.getPrincipal();
        Passenger passenger = passengerService.findById(user.getId()).orElseThrow(() -> new RuntimeException("No passenger found!"));
        rideService.createRideRequest(MappingUtils.convertRideRequestDTOtoEntity(rideRequest), passenger);
        return new ResponseOk("ok");
	}


    @PutMapping("/abandon/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseOk abandonRide(@PathVariable("id") UUID rideId, @RequestBody(required = false) String reason, Authentication authentication) {
        Optional<Driver> driver = driverService.findById(((User) authentication.getPrincipal()).getId());
        if (driver.isPresent()) {
            rideService.abandonRideDriver(rideId, reason, driver.get());
        } else {
            Passenger passenger = passengerService.findById(((User) authentication.getPrincipal()).getId()).orElseThrow(() -> new UserNotFoundException("No such user found!"));
            rideService.abandonRidePassenger(rideId, reason, passenger);
        }
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
