package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.User;
import com.nwt.juber.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("notifications")
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PatchMapping("/read-all")
    public ResponseOk markAllAsRead(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        notificationService.markNotificationsAsRead(user);
        return new ResponseOk("Notifications marked as read.");
    }

    @GetMapping("/")
    public List<TransferredNotification> getNotifications(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return notificationService.getNotifications(user);
    }

}

