package com.nwt.juber.model.notification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.nwt.juber.dto.notification.EveryoneAcceptedRide;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Ride;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("EveryoneAcceptedRideNotification")
public class EveryoneAcceptedRideNotification extends PersistedNotification {

    @ManyToOne
    private Ride ride;

    @Override
    public TransferredNotification convertToTransferred() {
    	EveryoneAcceptedRide transferred = new EveryoneAcceptedRide();
        transferred.setDate(this.getCreated());
        transferred.setNotificationStatus(this.getStatus());
        if(ride!=null) {
        	LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        	long minutes = ChronoUnit.MINUTES.between(now, ride.getStartTime());
        	transferred.setUntilDriverArival(minutes);
        }
        
        return transferred;
    }
}
