package com.nwt.juber.dto.response;

import com.nwt.juber.dto.PersonDTO;
import com.nwt.juber.model.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverInfoResponse {
    private PersonDTO profile;
    private DriverStatus status;
    private String startPlaceName;
    private String endPlaceName;
}
