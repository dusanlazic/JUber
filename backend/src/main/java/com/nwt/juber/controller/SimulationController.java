package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.DriverLocationUpdateDTO;
import com.nwt.juber.dto.SimulationInfo;
import com.nwt.juber.service.DriverService;
import com.nwt.juber.service.RideService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "simulation")
public class SimulationController {

    @Autowired
    RideService rideService;

    @Autowired
    DriverService driverService;

    @GetMapping(value = "drivers")
    public List<SimulationInfo> getDriversInfo() {
        return driverService.getSimulationInfo();
    }

    @PostMapping(value = "/driver-location")
    public ResponseOk setDriverLocation(@RequestBody DriverLocationUpdateDTO update) {
        driverService.updateLocation(update.getUsername(), update.getLongitude(), update.getLatitude());
        return new ResponseOk("Success");
    }

//    @PutMapping("/start-ride/{id}")
//    public ResponseOk startRide(@PathVariable("id") UUID rideId) {
//        rideService.startRide(rideId);
//        return new ResponseOk("ok");
//    }
//
//    @PutMapping("/end-ride/{id}")
//    public ResponseOk endRide(@PathVariable("id") UUID rideId) {
//        rideService.endRide(rideId);
//        return new ResponseOk("ok");
//    }



}
