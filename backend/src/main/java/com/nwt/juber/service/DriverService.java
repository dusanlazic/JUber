package com.nwt.juber.service;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.SimulationInfo;
import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Ride;
import com.nwt.juber.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public List<SimulationInfo> getSimulationInfo() {
        List<Driver> drivers = driverRepository.findAll();
        List<SimulationInfo> dtos = drivers
                                        .stream()
                                        .map(this::driverToSimulationInfo)
                                        .toList();
        return dtos;
    }

    private SimulationInfo driverToSimulationInfo(Driver driver) {
        SimulationInfo dto = new SimulationInfo();
        dto.setUsername(driver.getMail());
        dto.setLongitude(driver.getVehicle().getLongitude());
        dto.setLatitude(driver.getVehicle().getLatitude());
        Optional<Ride> rideToSim = driverRepository.findRouteForSimulation(driver.getMail());
        dto.setRoute(rideToSim.map(Ride::getRoute).orElse(null));
        dto.setStatus(rideToSim.map(Ride::getRideStatus).orElse(null));
        dto.setRideId(rideToSim.map(Ride::getId).orElse(null));
        return dto;
    }

    public void updateLocation(String username, Double longitude, Double latitude) {
        Driver driver = driverRepository.findById(username).orElseThrow(() -> new RuntimeException("No driver with username: " + username));
        driver.getVehicle().setLongitude(longitude);
        driver.getVehicle().setLatitude(latitude);
        driverRepository.save(driver);
    }
}
