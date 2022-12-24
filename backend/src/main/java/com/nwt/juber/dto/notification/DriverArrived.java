package com.nwt.juber.dto.notification;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class DriverArrived extends TransferredNotification {
    @Setter(AccessLevel.NONE)
    private final NotificationType type = NotificationType.DRIVER_ARRIVED;
    private String driverName;
    private String driverImageUrl;
}
