package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.message.PersonLocationMessage;
import com.nwt.juber.dto.request.DriverRegistrationRequest;
import com.nwt.juber.dto.response.*;
import com.nwt.juber.model.Driver;
import com.nwt.juber.service.AccountService;
import com.nwt.juber.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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


    @GetMapping("/all-locations")
    public List<PersonLocationMessage> getAllLocations() {
    	return driverService.getAllLocations();
    }

//    @GetMapping("/location/{email}")
//    public PersonLocationMessage getAllLocations(@PathVariable("email") String email) {
//        return driverService.locationForDriverEmail(email);
//    }

    @GetMapping("/location/{id}")
    public PersonLocationMessage getAllLocations(@PathVariable("id") String id) {
        UUID uid = UUID.fromString(id);
        return driverService.locationForDriverId(uid);
    }

    @GetMapping("/statuses")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BriefDriverStatusResponse> getAllBriefStatuses() {
        return driverService.getAllBriefStatuses();
    }

    @GetMapping("/{driverId}/info")
    @PreAuthorize("hasRole('ADMIN')")
    public DriverInfoResponse getDriverInfo(@PathVariable UUID driverId) {
        return driverService.getDriverInfo(driverId);
    }

    @GetMapping("/{driverId}/rides")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PastRidesResponse> getDriversPastRides(@PathVariable UUID driverId) {
        return driverService.getDriversPastRides(driverId);
    }

    @GetMapping("/{driverId}/reviews")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RideReviewResponse> getDriversReviews(@PathVariable UUID driverId) {
        return driverService.getDriversReviews(driverId);
    }
}
