package com.nwt.juber.model.notification;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.nwt.juber.dto.notification.DriverArrived;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Ride;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("DriverArrivedNotification")
public class DriverArrivedNotification extends PersistedNotification {

    @ManyToOne
    private Ride ride;

    @Override
    public TransferredNotification convertToTransferred() {
        DriverArrived transferred = new DriverArrived();
        transferred.setDate(this.getCreated());
        if(ride!=null && ride.getDriver()!=null) {
        	transferred.setDriverName(ride.getDriver().getName());
            transferred.setDriverImageUrl(ride.getDriver().getImageUrl());
        }
        
        return transferred;
    }
}
