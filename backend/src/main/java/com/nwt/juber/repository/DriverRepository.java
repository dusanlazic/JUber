package com.nwt.juber.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nwt.juber.model.Driver;
import com.nwt.juber.model.DriverStatus;

public interface DriverRepository extends JpaRepository<Driver, UUID> {
	
	Optional<Driver> findByEmail(String email);
	
	List<Driver> findByStatus(DriverStatus status);
	
	// List<Driver> findActiveInLast24h();
}
