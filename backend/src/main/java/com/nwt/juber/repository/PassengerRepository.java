package com.nwt.juber.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nwt.juber.model.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, UUID> {

	Optional<Passenger> findByEmail(String email);

}
