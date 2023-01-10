package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.RideCancelled;
import com.nwt.juber.dto.notification.RideStatusUpdated;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
@Entity
@Data
@NoArgsConstructor
public class RideCancelledNotification extends PersistedNotification {

    @ManyToOne
    private Ride ride;

    @ManyToOne
    private Passenger canceler;

    @Override
    public TransferredNotification convertToTransferred() {
        RideCancelled transferred = new RideCancelled();
        transferred.setDate(this.getCreated());
        String fullname = this.getCanceler().getFirstName() + " " + this.getCanceler().getLastName();
        transferred.setCanceler(fullname);
        transferred.setCancelerImageUrl(this.getCanceler().getImageUrl());
        transferred.setRideId(this.getRide().getId());
        return transferred;
    }
}
