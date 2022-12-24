package com.nwt.juber.model.notification;

import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
public abstract class PersistedNotification {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @ManyToOne
    private User receiver;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.UNREAD;

    @CreationTimestamp
    private Date created;

    public abstract TransferredNotification convertToTransferred();
}
