package com.nwt.juber.repository;

import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    @Query("update Ride r set r.rideStatus = :status where r.id = :rideId")
    void setRideStatus(UUID rideId, RideStatus status);

    @Query("select r from Ride r where r.driver = :driver and " +
            "(r.rideStatus = 1 or r.rideStatus = 2 or r.rideStatus = 3)")
    Ride getActiveRideForDriver(Driver driver);

    @Query("select r from Ride r where :passenger member of r.passengers and " +
            "(r.rideStatus = 0 or r.rideStatus = 1 or r.rideStatus = 2 or r.rideStatus = 3 or r.rideStatus = 6)")
    Ride getActiveRideForPassenger(Passenger passenger);

    @Query("select r from Ride r where r.driver = :driver and (r.rideStatus = 6)")
    Ride getScheduledRideForDriver(Driver driver);
}
