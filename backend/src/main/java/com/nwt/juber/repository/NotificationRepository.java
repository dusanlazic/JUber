package com.nwt.juber.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nwt.juber.model.notification.NotificationResponse;
import com.nwt.juber.model.notification.NotificationStatus;
import com.nwt.juber.model.notification.PersistedNotification;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<PersistedNotification, UUID> {

    List<PersistedNotification> findByStatusAndCreatedBefore(NotificationStatus status, Date date);

    default List<PersistedNotification> findReadAndCreatedBefore(Date date) {
        return findByStatusAndCreatedBefore(NotificationStatus.READ, date);
    }
    
    @Modifying
    @Query("update PersistedNotification notif set notif.response = ?2 where notif.id = ?1")
    void updateNotificationResponse(UUID id, NotificationResponse response);
}
