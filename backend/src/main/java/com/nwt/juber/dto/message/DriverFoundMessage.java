package com.nwt.juber.dto.message;

import com.nwt.juber.dto.PersonDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DriverFoundMessage {
    private PersonDTO driver;
    private String rideId;

}
