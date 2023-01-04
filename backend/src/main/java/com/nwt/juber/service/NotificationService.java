package com.nwt.juber.service;

import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.User;
import com.nwt.juber.model.notification.NotificationStatus;
import com.nwt.juber.model.notification.PersistedNotification;
import com.nwt.juber.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final String NOTIFICATIONS_DESTINATION = "/queue/notifications";

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    public void markNotificationsAsRead(User user) {
        List<PersistedNotification> notifications = user.getNotifications();
        notifications.forEach(n -> n.setStatus(NotificationStatus.READ));

        notificationRepository.saveAll(notifications);
    }

    public List<TransferredNotification> getNotifications(User user) {
        return user.getNotifications().stream()
                .map(PersistedNotification::convertToTransferred)
                .sorted(Comparator.comparing(TransferredNotification::getDate).reversed())
                .collect(Collectors.toList());
    }

    public void send(PersistedNotification notification, User receiver) {
        notification.setReceiver(receiver);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(receiver.getUsername(), NOTIFICATIONS_DESTINATION, notification.convertToTransferred());
    }

    @Scheduled(cron = "* 2 * * * *")
    private void removeOldNotifications() {
        Instant limit = Instant.now().minus(2, ChronoUnit.DAYS);
        List<PersistedNotification> oldNotifications = notificationRepository.findReadAndCreatedBefore(Date.from(limit));

        notificationRepository.deleteAll(oldNotifications);
    }
}
