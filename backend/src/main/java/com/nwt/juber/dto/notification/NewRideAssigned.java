package com.nwt.juber.dto.notification;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class NewRideAssigned extends TransferredNotification {
    @Setter(AccessLevel.NONE)
    private final NotificationType type = NotificationType.RIDE_ASSIGNED;
    private Integer passengerCount;
    private String startLocationName;
}
