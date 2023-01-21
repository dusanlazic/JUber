package com.nwt.juber.dto.notification;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class RideReminder extends TransferredNotification {
    @Setter(AccessLevel.NONE)
    private final NotificationType type = NotificationType.RIDE_REMINDER;
    private String startingLocation;
    private LocalDateTime startTime;

    private UUID rideId;
}
