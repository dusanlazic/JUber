package com.nwt.juber.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nwt.juber.model.RideReview;
import org.springframework.stereotype.Repository;

@Repository
public interface RideReviewRepository extends JpaRepository<RideReview, UUID>{

       Set<RideReview> getRideReviewsByRideId(UUID rideId);

       @Query(value = "select r from RideReview r where r.ride.driver.id = :driverId")
       Set<RideReview> getRideReviewsByDriverId(UUID driverId);

       Set<RideReview> getRideReviewsByReviewerId(UUID reviewerId);
       
       Optional<RideReview> getRideReviewsByReviewerIdAndRideId(UUID reviewerId, UUID rideId);
}
