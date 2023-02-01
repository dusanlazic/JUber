package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AdditionalRideRequestsDTO {
    @NotNull
    private boolean babyFriendly;

    @NotNull
    private boolean petFriendly;
    
    @NotNull
    private VehicleTypeDTO vehicleType;
}
