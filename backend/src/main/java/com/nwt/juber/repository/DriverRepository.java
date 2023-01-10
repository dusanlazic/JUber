package com.nwt.juber.repository;

import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    @Query(value = "select d from Driver d where not exists (select r from Ride r where r.driver = d and r.rideStatus = 1 or r.rideStatus = 2)")
    public List<Driver> findAllWithNoRides();

    @Query(value = "select d from Driver d where not exists (select r from Ride r where r.driver = d and r.rideStatus = 1)")
    public List<Driver> findAllWithNoFutureRides();

    @Query(value = "select r from Ride r where r.driver.email = :username and (r.rideStatus = 1 or r.rideStatus = 2) order by r.rideStatus desc")
    public Optional<Ride> findRouteForSimulation(String username);

}
