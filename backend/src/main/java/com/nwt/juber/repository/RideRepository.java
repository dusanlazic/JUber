package com.nwt.juber.repository;

import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    @Query("update Ride r set r.rideStatus = :status where r.id = :rideId")
    void setRideStatus(UUID rideId, RideStatus status);

}
