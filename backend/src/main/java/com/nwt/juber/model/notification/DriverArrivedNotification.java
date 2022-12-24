package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.DriverArrived;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Ride;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
public class DriverArrivedNotification extends PersistedNotification {

    @ManyToOne
    private Ride ride;

    @Override
    public TransferredNotification convertToTransferred() {
        DriverArrived transferred = new DriverArrived();
        transferred.setDate(this.getCreated());
        transferred.setDriverName(ride.getDriver().getName());
        transferred.setDriverImageUrl(ride.getDriver().getImageUrl());

        return transferred;
    }
}
