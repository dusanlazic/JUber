package com.nwt.juber.service;

import com.nwt.juber.dto.response.BalanceResponse;
import com.nwt.juber.dto.response.DepositAddressResponse;
import com.nwt.juber.exception.NotImplementedException;
import com.nwt.juber.model.DepositAddress;
import com.nwt.juber.model.DepositAddressStatus;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.repository.DepositAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Autowired
    private DepositAddressRepository depositAddressRepository;

    public BalanceResponse checkBalance(Passenger passenger) {
        return new BalanceResponse(passenger.getBalance());
    }

    public DepositAddressResponse requestDepositAddress(Passenger passenger) {
        DepositAddress depositAddress = depositAddressRepository.findFirstByPassengerAndIsPending(passenger)
                                .orElse(depositAddressRepository.findFirstUnassigned().orElse(null));

        if (depositAddress == null) {
            generateNewDepositAddresses();
            depositAddress = depositAddressRepository.findFirstUnassigned().get();
        }

        depositAddress.setPassenger(passenger);
        depositAddress.setStatus(DepositAddressStatus.PENDING);
        depositAddressRepository.save(depositAddress);
        return new DepositAddressResponse(depositAddress.getEthAddress());
    }

    private void generateNewDepositAddresses() {
        throw new NotImplementedException();
    }

    private BigDecimal calculateConversionRate() {
        // TODO: Make periodical API calls to determine WEI to RSD rate
        return BigDecimal.valueOf(0.47);
    }
}
