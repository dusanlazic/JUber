package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.exception.NotImplementedException;

import javax.persistence.Entity;

@Entity
public class GeneralNotification extends PersistedNotification {

    @Override
    public TransferredNotification convertToTransferred() {
        throw new NotImplementedException();
    }
}
