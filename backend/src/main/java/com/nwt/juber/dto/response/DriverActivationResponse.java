package com.nwt.juber.dto.response;

import java.sql.Timestamp;

import com.nwt.juber.model.DriverStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverActivationResponse {

    private DriverStatus status;
    private Timestamp overtimeEnd;
}
