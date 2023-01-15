package com.nwt.juber.dto.notification;

import lombok.Data;

import java.util.Date;

import com.nwt.juber.model.notification.NotificationStatus;

@Data
public abstract class TransferredNotification {
    private Date date;
    private NotificationStatus notificationStatus;
}
