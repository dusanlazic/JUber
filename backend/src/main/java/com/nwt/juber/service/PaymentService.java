package com.nwt.juber.service;

import com.nwt.juber.config.AppProperties;
import com.nwt.juber.dto.response.BalanceResponse;
import com.nwt.juber.dto.response.DepositAddressResponse;
import com.nwt.juber.dto.response.cryptocompare.PriceResponse;
import com.nwt.juber.dto.response.etherscan.AccountBalancePair;
import com.nwt.juber.dto.response.etherscan.BalancesResponse;
import com.nwt.juber.model.DepositAddress;
import com.nwt.juber.model.DepositAddressStatus;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.repository.DepositAddressRepository;
import com.nwt.juber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
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
        Instant limit = Instant.now().minusSeconds(appProperties.getPayment().getPendingTimeoutSeconds());
        List<DepositAddress> recentPendingAddresses = depositAddressRepository.findPendingAndModifiedAfter(Date.from(limit));

        if (recentPendingAddresses.size() > 0) {
            List<AccountBalancePair> balances = fetchBalancesFromEtherscan(recentPendingAddresses);

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

    private List<AccountBalancePair> fetchBalancesFromEtherscan(List<DepositAddress> pendingAddresses) {
        WebClient webClient = WebClient.create(appProperties.getPayment().getEtherscanUrl());
        String addresses = pendingAddresses.stream().map(DepositAddress::getEthAddress).collect(Collectors.joining(","));
        BalancesResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("module", "account")
                        .queryParam("action", "balancemulti")
                        .queryParam("address", addresses)
                        .queryParam("tag", "latest")
                        .queryParam("apikey", appProperties.getPayment().getEtherscanKey())
                        .build())
                .retrieve()
                .bodyToMono(BalancesResponse.class)
                .block();
        if (response == null)
            return new LinkedList<>();

        return response.getResult();
    }

    private BigDecimal fetchEthPriceFromCryptocompare() {
        WebClient webClient = WebClient.create(appProperties.getPayment().getCryptocompareUrl());
        PriceResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("fsym", "ETH")
                        .queryParam("tsyms", "RSD")
                        .queryParam("api_key", appProperties.getPayment().getCryptocompareKey())
                        .build())
                .retrieve()
                .bodyToMono(PriceResponse.class)
                .block();
        if (response == null)
            return null;

        return BigDecimal.valueOf(response.getValue());
    }

    private DepositAddress generateNewDepositAddresses() {
        SecureRandom randomness = new SecureRandom();
        List<DepositAddress> addresses = new ArrayList<>();

        try {
            for (int i = 0; i < 100; i++) {
                ECKeyPair keyPair = Keys.createEcKeyPair(randomness);
                String privateKey = keyPair.getPrivateKey().toString(16);
                Credentials credentials = Credentials.create(privateKey);
                System.out.println(credentials.getAddress() + " " + privateKey);
                addresses.add(new DepositAddress(credentials.getAddress()));
            }
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException ignored) {

        }

        depositAddressRepository.saveAll(addresses);
        return depositAddressRepository.findFirstUnassigned().get();
    }

    private BigDecimal convertFromWei(BigInteger value) {
        return new BigDecimal(value)
                .movePointLeft(18)
                .multiply(fetchEthPriceFromCryptocompare())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
