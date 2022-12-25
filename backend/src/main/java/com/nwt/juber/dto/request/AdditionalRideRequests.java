package com.nwt.juber.dto.request;

import com.nwt.juber.model.VehicleType;

import lombok.Data;

@Data
public class AdditionalRideRequests {

	public boolean babyFriendly;
	public boolean petFriendly;
	public VehicleType vehicleType;
}
