package com.nwt.juber.repository;

import com.nwt.juber.model.DepositAddress;
import com.nwt.juber.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepositAddressRepository extends JpaRepository<DepositAddress, UUID> {

    Optional<DepositAddress> findFirstByPassengerAndPaidWeiIsNull(Passenger passenger);

    Optional<DepositAddress> findFirstByPassengerIsNull();

}
