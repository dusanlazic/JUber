package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.RideReminder;
import com.nwt.juber.dto.notification.TransferredNotification;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
public class RideReminderNotification extends PersistedNotification {

    private Integer minutesLeft;

    @Override
    public TransferredNotification convertToTransferred() {
        RideReminder transferred = new RideReminder();
        transferred.setDate(this.getCreated());
        transferred.setMinutesLeft(minutesLeft);

        return transferred;
    }
}
