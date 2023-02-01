package com.nwt.juber.dto.request;

import com.nwt.juber.model.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdditionalRideRequests {
	public boolean babyFriendly;
	public boolean petFriendly;
	public VehicleType vehicleType;
}
