package com.nwt.juber.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nwt.juber.exception.EndRideException;
import com.nwt.juber.exception.StartRideException;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideStatus;
import com.nwt.juber.repository.RideRepository;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;
    
	public void startRide(UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new StartRideException("No ride with id: " + rideId));
        if (ride.getRideStatus() != RideStatus.ACCEPTED) {
            throw new StartRideException("Ride not accepted!");
        }
        ride.setRideStatus(RideStatus.IN_PROGRESS);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);
    }

    public void endRide(UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        if (ride.getRideStatus() != RideStatus.IN_PROGRESS)  {
            throw new StartRideException("Ride not in progress!");
        }
        ride.setRideStatus(RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());
        rideRepository.save(ride);
    }
}
