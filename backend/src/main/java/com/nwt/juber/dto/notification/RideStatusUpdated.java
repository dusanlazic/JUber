package com.nwt.juber.dto.notification;

import com.nwt.juber.model.RideStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class RideStatusUpdated extends TransferredNotification {
    @Setter(AccessLevel.NONE)
    private final NotificationType type = NotificationType.RIDE_STATUS_UPDATED;
    private RideStatus status;
}
