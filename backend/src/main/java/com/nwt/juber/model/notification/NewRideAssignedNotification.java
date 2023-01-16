package com.nwt.juber.model.notification;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.nwt.juber.dto.notification.NewRideAssigned;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Ride;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("NewRideAssignedNotification")
public class NewRideAssignedNotification extends PersistedNotification {

    @ManyToOne
    private Ride ride;

    @Override
    public TransferredNotification convertToTransferred() {
        NewRideAssigned transferred = new NewRideAssigned();
        transferred.setDate(this.getCreated());
        transferred.setNotificationStatus(this.getStatus());
        transferred.setPassengerCount(ride.getPassengers().size());
        transferred.setStartLocationName(ride.getPlaces().get(0).getName());

        return transferred;
    }
}