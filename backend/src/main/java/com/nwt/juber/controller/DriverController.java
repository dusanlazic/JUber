package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.DriverRegistrationRequest;
import com.nwt.juber.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "accounts/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class DriverController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/")
    public ResponseOk register(@Valid @RequestBody DriverRegistrationRequest registrationRequest) {
        accountService.registerDriver(registrationRequest);
        return new ResponseOk("Driver registered successfully.");
    }

}
