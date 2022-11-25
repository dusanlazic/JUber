package com.nwt.juber.controller;

import com.nwt.juber.dto.response.BalanceResponse;
import com.nwt.juber.dto.response.DepositAddressResponse;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "payment", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('PASSENGER')")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/balance")
    public BalanceResponse checkBalance(Authentication authentication) {
        Passenger passenger = (Passenger) authentication.getPrincipal();
        return paymentService.checkBalance(passenger);
    }

    @PostMapping("/deposit")
    public DepositAddressResponse requestDepositAddress(Authentication authentication) {
        Passenger passenger = (Passenger) authentication.getPrincipal();
        return paymentService.requestDepositAddress(passenger);
     }
}
