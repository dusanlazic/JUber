package com.nwt.juber.dto;

import com.nwt.juber.model.Driver;
import com.nwt.juber.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRideDTO {
    private Driver driver;
    private Ride ride;
}
