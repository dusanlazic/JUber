package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.DriverRegistrationRequest;
import com.nwt.juber.service.AccountService;
import com.nwt.juber.service.DriverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "accounts/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private DriverService driverService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk register(@Valid @RequestBody DriverRegistrationRequest registrationRequest) {
        accountService.registerDriver(registrationRequest);
        return new ResponseOk("Driver registered successfully.");
    }
    
    
    @PatchMapping("/activate") 
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseOk activateDriver(@Valid @RequestBody String driverEmail) {
    	driverService.activateDriver(driverEmail);
        return new ResponseOk("Driver activated successfully.");
    }

    @PatchMapping("/inactivate") 
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseOk inactivateDriver(@Valid @RequestBody String driverEmail) {
    	driverService.inactivateDriver(driverEmail);
        return new ResponseOk("Driver inactivated successfully.");

    }
}
