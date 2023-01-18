package com.nwt.juber.model.notification;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.nwt.juber.dto.notification.RideCancelled;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;

import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("RideCancelledNotification")
public class RideCancelledNotification extends PersistedNotification {

    @ManyToOne
    private Ride ride;

    @ManyToOne
    private Passenger canceler;

    @Override
    public TransferredNotification convertToTransferred() {
        RideCancelled transferred = new RideCancelled();
        transferred.setDate(this.getCreated());
        transferred.setNotificationStatus(this.getStatus());
        String fullname = this.getCanceler().getFirstName() + " " + this.getCanceler().getLastName();
        transferred.setCanceler(fullname);
        transferred.setCancelerImageUrl(this.getCanceler().getImageUrl());
        transferred.setRideId(this.getRide().getId());
        return transferred;
    }
}
