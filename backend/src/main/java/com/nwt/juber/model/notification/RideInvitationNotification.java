package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.RideInvitation;
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
public class RideInvitationNotification extends PersistedNotification {

    @ManyToOne
    private Passenger inviter;

    @ManyToOne
    private Ride ride;

    @Override
    public TransferredNotification convertToTransferred() {
        RideInvitation transferred = new RideInvitation();
        transferred.setDate(this.getCreated());
        transferred.setRideId(ride.getId());
        transferred.setInviterName(inviter.getName());
        transferred.setInviterImageUrl(inviter.getImageUrl());

        return transferred;
    }
}
