package com.nwt.juber.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.nwt.juber.dto.DriverRideDTO;
import com.nwt.juber.dto.message.PersonLocationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverStatus;
import com.nwt.juber.model.Ride;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {

    @Query(value = "select d from Driver d where not exists (select r from Ride r where r.driver = d and r.rideStatus = 1 or r.rideStatus = 2)")
    public List<Driver> findAllWithNoRides();

    @Query(value = "select d from Driver d where not exists (select r from Ride r where r.driver = d and r.rideStatus = 1)")
    public List<Driver> findAllWithNoFutureRides();

    @Query(value = "select r from Ride r where r.driver.email = :username and (r.rideStatus = 1 or r.rideStatus = 2 or r.rideStatus = 3) order by r.rideStatus desc")
    public Optional<Ride> findRideForSimulation(String username);

	List<Driver> findByStatus(DriverStatus status);

    Optional<Driver> findByEmail(String username);

    @Query(value = "select new com.nwt.juber.dto.message.PersonLocationMessage(d.email, d.vehicle.latitude, d.vehicle.longitude) from Driver d where d.status = 'ACTIVE'")
    List<PersonLocationMessage> findAllLocations();

    @Query(value = "select new com.nwt.juber.dto.message.PersonLocationMessage(d.email, d.vehicle.latitude, d.vehicle.longitude) from Driver d where d.email = :email")
    PersonLocationMessage locationForDriverEmail(String email);

    @Query(value = "select new com.nwt.juber.dto.message.PersonLocationMessage(d.email, d.vehicle.latitude, d.vehicle.longitude) from Driver d where d.id = :id")
    PersonLocationMessage locationForDriverId(UUID id);

    @Query(value = "select d from Driver d where d.status = 'ACTIVE' and " +
            "(select count(r) from Ride r where r.driver = d and r.rideStatus = 1 or r.rideStatus = 2 or r.rideStatus = 3) = 0") // current rides
    List<Driver> findAvailableDrivers(Ride ride);

    @Query(value = "select new com.nwt.juber.dto.DriverRideDTO(d, r) from Driver d, Ride r where d.status = 'ACTIVE' and r.driver = d and" +
            "(select count(t) from Ride t where t.driver = d and t.rideStatus = 1 or t.rideStatus = 2) = 1 and " + // current rides
            "(select count(t) from Ride t where t.driver = t and t.rideStatus = 6) = 0") // scheduled rides
    List<DriverRideDTO> findUnavailableDriversWithNoFutureRides(Ride ride);


}