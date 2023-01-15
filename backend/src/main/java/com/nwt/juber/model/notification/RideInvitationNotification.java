package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.RideInvitation;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.engine.spi.EntityEntryExtraState;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("RideInvitationNotification")
public class RideInvitationNotification extends PersistedNotification {

    @ManyToOne
    private Passenger inviter;

    @ManyToOne
    private Passenger invitee;

    @ManyToOne
    private Ride ride;

    private Double balance;
    
    @Enumerated(EnumType.STRING)
    private NotificationResponse response = NotificationResponse.NO_RESPONSE;

    @Override
    public TransferredNotification convertToTransferred() {
        RideInvitation transferred = new RideInvitation();
        transferred.setDate(this.getCreated());
        transferred.setNotificationStatus(this.getStatus());
        transferred.setRideId(ride.getId());
        transferred.setInviterName(inviter.getName());
        transferred.setInviterImageUrl(inviter.getImageUrl());
        transferred.setBalance(invitee.getBalance().doubleValue());
        transferred.setStartLocationName(ride.getPlaces().get(0).getName());
        transferred.setResponse(this.getResponse());
        transferred.setNotificationId(getId());
        return transferred;
    }
}
