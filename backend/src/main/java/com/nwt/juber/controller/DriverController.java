package com.nwt.juber.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.DriverRegistrationRequest;
import com.nwt.juber.dto.response.DriverActivationResponse;
import com.nwt.juber.model.Driver;
import com.nwt.juber.service.AccountService;
import com.nwt.juber.service.DriverService;

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
    public DriverActivationResponse activateDriver(@Valid @RequestBody String driverEmail) {
    	Driver driver = driverService.activateDriver(driverEmail);
    	return new DriverActivationResponse(driver.getStatus(), driver.getDriverShifts().get(0).getStartOfShift());
    }

    @PatchMapping("/inactivate") 
    @PreAuthorize("hasRole('DRIVER')")
    public DriverActivationResponse inactivateDriver(@Valid @RequestBody String driverEmail) {
    	Driver driver = driverService.inactivateDriver(driverEmail);
    	return new DriverActivationResponse(driver.getStatus(), driver.getDriverShifts().get(0).getStartOfShift());
    }
}
