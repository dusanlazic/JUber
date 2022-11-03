package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.LocationDTO;
import com.nwt.juber.dto.SimulationInfo;
import com.nwt.juber.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping(value = "simulation-info")
    public List<SimulationInfo> getSimulationInfo() {
        return driverService.getSimulationInfo();
    }

    @PutMapping(value = "location/{username}")
    public ResponseOk setDriverLocation(@PathVariable String username, @RequestBody LocationDTO location) {
        driverService.updateLocation(username, location.getLongitude(), location.getLatitude());
        return new ResponseOk("ok");
    }

}
