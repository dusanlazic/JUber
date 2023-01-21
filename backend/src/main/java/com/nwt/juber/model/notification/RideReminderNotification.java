package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.RideReminder;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Ride;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("RideReminderNotification")
public class RideReminderNotification extends PersistedNotification {

    private String startingLocation;
    private LocalDateTime startTime;
    @ManyToOne
    private Ride ride;

    @Override
    public TransferredNotification convertToTransferred() {
        RideReminder transferred = new RideReminder();
        transferred.setDate(this.getCreated());
        transferred.setNotificationStatus(this.getStatus());
        transferred.setStartingLocation(this.getStartingLocation());
        transferred.setStartTime(this.getStartTime());
        transferred.setRideId(this.getRide().getId());
        return transferred;
    }
}
