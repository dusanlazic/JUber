package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SavedRouteResponse {
    private UUID rideId;
    private List<String> places;
    private Double fare;
}
