package com.nwt.juber.repository;

import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PassengerRepository extends JpaRepository<Passenger, UUID> {
}
