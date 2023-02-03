package com.nwt.juber.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.nwt.juber.dto.DriverRideDTO;
import com.nwt.juber.dto.message.PersonLocationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverStatus;
import com.nwt.juber.model.Ride;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {


    @Query(value = "select r from Ride r where r.driver.email = :username and (r.rideStatus = 1 or r.rideStatus = 2 or r.rideStatus = 3) order by r.rideStatus desc")
    public Optional<Ride> findRideForSimulation(String username);

	List<Driver> findByStatus(DriverStatus status);

    Optional<Driver> findByEmail(String email);

    @Query(value = "select new com.nwt.juber.dto.message.PersonLocationMessage(d.email, d.vehicle.latitude, d.vehicle.longitude) from Driver d where d.status = 'ACTIVE'")
    List<PersonLocationMessage> findAllLocations();

    @Query(value = "select new com.nwt.juber.dto.message.PersonLocationMessage(d.email, d.vehicle.latitude, d.vehicle.longitude) from Driver d where d.email = :email")
    PersonLocationMessage locationForDriverEmail(String email);

    @Query(value = "select new com.nwt.juber.dto.message.PersonLocationMessage(d.email, d.vehicle.latitude, d.vehicle.longitude) from Driver d where d.id = :id")
    PersonLocationMessage locationForDriverId(UUID id);

    @Query(value = "select d from Driver d where d.status = 'ACTIVE' and d.blocked = FALSE and " +
            "(select count(r) from Ride r where r.driver = d and r.rideStatus = 1 or r.rideStatus = 2 or r.rideStatus = 3) = 0") // current rides
    List<Driver> findAvailableDrivers();

    @Query(value = "select new com.nwt.juber.dto.DriverRideDTO(d, r) from Driver d join Ride r on r.driver = d where d.blocked = false and d.status = 'ACTIVE' and " +
            "(r.rideStatus = 1 or r.rideStatus = 2 or r.rideStatus = 3) and" +
            "(select count(t) from Ride t where t.driver = d and (t.rideStatus = 1 or t.rideStatus = 2 or t.rideStatus = 3)) = 1 and " + // current rides
            "(select count(k) from Ride k where k.driver = d and k.rideStatus = 6) = 0 group by d") // scheduled rides
    List<DriverRideDTO> findUnavailableDriversWithNoFutureRides();


}