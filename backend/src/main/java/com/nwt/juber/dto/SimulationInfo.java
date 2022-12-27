package com.nwt.juber.dto;

import com.nwt.juber.model.Place;
import com.nwt.juber.model.RideStatus;
import com.nwt.juber.model.Route;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SimulationInfo {
    private String username;
    private Double longitude;
    private Double latitude;
    private RideStatus status;
    private UUID rideId;
    private List<Place> places;
}
