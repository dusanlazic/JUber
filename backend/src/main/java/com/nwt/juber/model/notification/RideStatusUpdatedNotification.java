package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.RideStatusUpdated;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Ride;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("RideStatusUpdatedNotification")
public class RideStatusUpdatedNotification extends PersistedNotification {

    @ManyToOne
    private Ride ride;

    @Override
    public TransferredNotification convertToTransferred() {
        RideStatusUpdated transferred = new RideStatusUpdated();
        transferred.setDate(this.getCreated());
        transferred.setNotificationStatus(this.getStatus());
        transferred.setStatus(ride.getRideStatus());

        return transferred;
    }
}
