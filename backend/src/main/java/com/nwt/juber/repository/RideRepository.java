package com.nwt.juber.repository;

import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RideRepository extends JpaRepository<Ride, UUID> {

    @Query("update Ride r set r.rideStatus = :status where r.id = :rideId")
    @Modifying
    void setRideStatus(UUID rideId, RideStatus status);

    @Query(value = "select * from Ride r where r.DRIVER_ID = ?1 and " +
            "(r.RIDE_STATUS = 1 or r.RIDE_STATUS = 2 or r.RIDE_STATUS = 3) order by r.RIDE_STATUS desc limit 1", nativeQuery = true)
    Ride getActiveRideForDriver(UUID driverId);

    @Query("select r from Ride r where :passenger member of r.passengers and " +
            "(r.rideStatus = 0 or r.rideStatus = 1 or r.rideStatus = 2 or r.rideStatus = 3 or r.rideStatus = 6)")
    Ride getActiveRideForPassenger(Passenger passenger);

    @Query("select r from Ride r where r.driver = :driver and (r.rideStatus = 6)")
    Ride getScheduledRideForDriver(Driver driver);

    @Query("select r from Ride r where :passenger member of r.passengers and r.rideStatus = 5")
    List<Ride> getPastRidesForPassenger(Passenger passenger);

    @Query("select r from Ride r where r.driver = :driver and r.rideStatus = 5")
    List<Ride> getPastRidesForDriver(Driver driver);

    @Query("select r from Ride r where :passenger member of r.passengers and r.rideStatus = 5 and r.endTime between :startTime and :endTime")
    List<Ride> getRidesForPassengerBetweenTimes(Passenger passenger, LocalDateTime startTime, LocalDateTime endTime);

    default List<Ride> getRidesForPassengerAtDate(Passenger passenger, LocalDate date) {
        return getRidesForPassengerBetweenTimes(passenger, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Query("select r from Ride r where r.driver = :driver and r.rideStatus = 5 and r.endTime between :startTime and :endTime")
    List<Ride> getRidesForDriverBetweenTimes(Driver driver, LocalDateTime startTime, LocalDateTime endTime);

    default List<Ride> getRidesForDriverAtDate(Driver driver, LocalDate date) {
        return getRidesForDriverBetweenTimes(driver, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Query("select r from Ride r where r.rideStatus = 5 and r.endTime between :startTime and :endTime")
    List<Ride> getRidesBetweenTimes(LocalDateTime startTime, LocalDateTime endTime);

    @Query("select r from Ride r join fetch Place p on p member of r.places join fetch Route ru on ru member of p.routes where r.id = :rideId")
    Ride getRideById(UUID rideId);

    default List<Ride> getRidesAtDate(LocalDate date) {
        return getRidesBetweenTimes(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

}
