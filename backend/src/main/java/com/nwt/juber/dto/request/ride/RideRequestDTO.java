package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RideRequestDTO {
    private RideDTO ride;
    private AdditionalRideRequestsDTO additionalRequests;
    private String scheduleTime;
    private List<String> passengerEmails;
}
