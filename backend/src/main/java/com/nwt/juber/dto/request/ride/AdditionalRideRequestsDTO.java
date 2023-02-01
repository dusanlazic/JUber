package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdditionalRideRequestsDTO {
    private boolean babyFriendly;
    private boolean petFriendly;
    private VehicleTypeDTO vehicleType;
}
