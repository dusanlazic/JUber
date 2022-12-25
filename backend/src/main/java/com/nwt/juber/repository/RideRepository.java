package com.nwt.juber.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nwt.juber.model.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, UUID> {

}
