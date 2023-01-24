package com.nwt.juber.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.nwt.juber.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nwt.juber.model.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, UUID> {

	Optional<Passenger> findByEmail(String email);

	@Query("select distinct (r) from Ride r join fetch Place pl on pl member of r.places join fetch Route ru on ru member of pl.routes, Passenger p where p = :passenger and r member of p.favouriteRoutes ")
	List<Ride> getFavouriteRides(Passenger passenger);

}
