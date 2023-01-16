package com.nwt.juber.dto.notification;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import com.nwt.juber.model.notification.NotificationResponse;

@Data
@NoArgsConstructor
public class RideInvitation extends TransferredNotification {
    @Setter(AccessLevel.NONE)
    private final NotificationType type = NotificationType.RIDE_INVITATION;
    private String inviterName;
    private String inviterImageUrl;
    private UUID rideId;
    private Double balance;
    private String startLocationName;
    private NotificationResponse response;
    private UUID notificationId;
}
