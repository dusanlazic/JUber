package com.nwt.juber.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.User;
import com.nwt.juber.model.notification.NotificationResponse;
import com.nwt.juber.model.notification.NotificationStatus;
import com.nwt.juber.model.notification.PersistedNotification;
import com.nwt.juber.repository.NotificationRepository;

@Service
public class NotificationService {

    private static final String NOTIFICATIONS_DESTINATION = "/queue/notifications";
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserService userService;

    public void markNotificationsAsRead(User user) {
    	System.out.println("MARK ALL AS READ");
    	user = userService.fetchUserWithNotificationsById(user.getId());
        List<PersistedNotification> notifications = user.getNotifications();
        notifications.forEach(n -> n.setStatus(NotificationStatus.READ));

        notificationRepository.saveAll(notifications);
    }

    public List<TransferredNotification> getNotifications(User user) {
    	user = userService.fetchUserWithNotificationsById(user.getId());
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
    public void removeOldNotifications() {
        Instant limit = Instant.now().minus(2, ChronoUnit.DAYS);
        List<PersistedNotification> oldNotifications = notificationRepository.findReadAndCreatedBefore(Date.from(limit));

        notificationRepository.deleteAll(oldNotifications);
    }

    @Transactional
	public void respondToNotification(UUID notificationId, NotificationResponse response) {
		notificationRepository.updateNotificationResponse(notificationId, response);
	}
}
