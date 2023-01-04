package com.nwt.juber.repository;

import com.nwt.juber.model.notification.NotificationStatus;
import com.nwt.juber.model.notification.PersistedNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<PersistedNotification, UUID> {

    List<PersistedNotification> findByStatusAndCreatedBefore(NotificationStatus status, Date date);

    default List<PersistedNotification> findReadAndCreatedBefore(Date date) {
        return findByStatusAndCreatedBefore(NotificationStatus.READ, date);
    }
}
