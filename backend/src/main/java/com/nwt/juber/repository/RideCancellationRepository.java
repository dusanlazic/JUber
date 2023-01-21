package com.nwt.juber.repository;

import com.nwt.juber.model.RideCancellation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RideCancellationRepository extends JpaRepository<RideCancellation, UUID> {
}
