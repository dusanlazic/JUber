package com.nwt.juber.service;

import com.nwt.juber.config.AppProperties;
import com.nwt.juber.dto.response.BalanceResponse;
import com.nwt.juber.dto.response.DepositAddressResponse;
import com.nwt.juber.dto.response.etherscan.AccountBalancePair;
import com.nwt.juber.dto.response.etherscan.BalancesResponse;
import com.nwt.juber.exception.NotImplementedException;
import com.nwt.juber.model.DepositAddress;
import com.nwt.juber.model.DepositAddressStatus;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.repository.DepositAddressRepository;
import com.nwt.juber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private DepositAddressRepository depositAddressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppProperties appProperties;

    public BalanceResponse checkBalance(Passenger passenger) {
        return new BalanceResponse(passenger.getBalance());
    }

    public DepositAddressResponse requestDepositAddress(Passenger passenger) {
        DepositAddress depositAddress = depositAddressRepository.findFirstByPassengerAndIsPending(passenger)
                       .orElseGet(() -> depositAddressRepository.findFirstUnassigned()
                       .orElseGet(this::generateNewDepositAddresses));

        depositAddress.setPassenger(passenger);
        depositAddress.setStatus(DepositAddressStatus.PENDING);
        depositAddress.updateModified(); // In case nothing is changed but we need it refreshed.
        depositAddressRepository.save(depositAddress);
        return new DepositAddressResponse(depositAddress.getEthAddress());
    }

    @Scheduled(cron = "*/5 * * * * *")
    private void processDeposits() {
        Instant limit = Instant.now().minusSeconds(appProperties.getEtherscan().getPendingTimeoutSeconds());
        List<DepositAddress> pendingAddresses = depositAddressRepository.findPendingAndModifiedAfter(Date.from(limit));
        WebClient webClient = WebClient.create(appProperties.getEtherscan().getUrl());

        if (pendingAddresses.size() > 0) {
            String addresses = pendingAddresses.stream().map(DepositAddress::getEthAddress).collect(Collectors.joining(","));
            BalancesResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("module", "account")
                            .queryParam("action", "balancemulti")
                            .queryParam("address", addresses)
                            .queryParam("tag", "latest")
                            .queryParam("apikey", appProperties.getEtherscan().getKey())
                            .build())
                    .retrieve()
                    .bodyToMono(BalancesResponse.class)
                    .block();
            if (response != null) {
                List<AccountBalancePair> balances = response.getResult();
                balances.forEach(addr -> {
                    DepositAddress depositAddress = depositAddressRepository.getByEthAddress(addr.getAccount()).get();
                    Passenger passenger = depositAddress.getPassenger();

                    if (!addr.getBalance().equals("0")) {
                        BigInteger weiBalance = new BigInteger(addr.getBalance());
                        BigDecimal currentBalance = passenger.getBalance();
                        BigDecimal updatedBalance = currentBalance.add(convertFromWei(weiBalance));

                        depositAddress.setStatus(DepositAddressStatus.PAID);
                        passenger.setBalance(updatedBalance);
                        depositAddressRepository.save(depositAddress);
                        userRepository.save(passenger);
                    }
                });
            }
        }
    }

    private DepositAddress generateNewDepositAddresses() {
        throw new NotImplementedException();
        // return depositAddressRepository.findFirstUnassigned().get();
    }

    private BigDecimal calculateConversionRate() {
        throw new NotImplementedException();
    }

    private BigDecimal convertFromWei(BigInteger value) {
        // TODO: Lookup current rate on API
        return new BigDecimal(value)
                .movePointLeft(18)
                .multiply(BigDecimal.valueOf(133357))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
