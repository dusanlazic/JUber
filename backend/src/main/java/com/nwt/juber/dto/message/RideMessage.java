package com.nwt.juber.dto.message;

import com.nwt.juber.dto.RideDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RideMessage {
    private RideDTO ride;
    private RideMessageType type;
}
