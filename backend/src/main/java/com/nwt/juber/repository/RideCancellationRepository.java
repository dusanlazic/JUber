package com.nwt.juber.repository;

import com.nwt.juber.model.RideCancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RideCancellationRepository extends JpaRepository<RideCancellation, UUID> {
}
