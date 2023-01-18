package com.nwt.juber.dto.request;

import java.util.List;

import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.Route;

import lombok.Data;

@Data
public class RideRequest {

	public Ride ride;
	public AdditionalRideRequests additionalRequests;
	public String scheduleTime;
	public List<String> passengerEmails;
	// public Double price ?
}
