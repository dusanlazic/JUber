package com.nwt.juber.dto.response;

import com.nwt.juber.model.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BriefDriverStatusResponse {
    private UUID driverId;
    private String fullName;
    private DriverStatus status;
    private String startPlaceName;
    private String endPlaceName;
}
