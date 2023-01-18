package com.nwt.juber.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.notification.TransferredNotification;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.User;
import com.nwt.juber.model.notification.DriverArrivedNotification;
import com.nwt.juber.model.notification.EveryoneAcceptedRideNotification;
import com.nwt.juber.model.notification.NotificationResponse;
import com.nwt.juber.service.NotificationService;

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
    
//    @PostMapping("/send")
//     public ResponseOk sendNotification(Authentication authentication) {
//	     DriverArrivedNotification notification = new DriverArrivedNotification();
//	     notification.setCreated(new Date());
//	     User user = (User) authentication.getPrincipal();
//	     notificationService.send(notification, user);
//	     return new ResponseOk("Notifications sent.");
//     }

    
//   @PostMapping("/send") 
//	public ResponseOk sendNotification(Authentication authentication) {
//    	EveryoneAcceptedRideNotification notification = new EveryoneAcceptedRideNotification();
//    	notification.setCreated(new Date());
//    	Ride ride = new Ride();
//    	LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//    	now = now.plusMinutes(20);
//    	ride.setStartTime(now);
//    	notification.setRide(ride);
//		User user = (User) authentication.getPrincipal();
//		notificationService.send(notification, user);
//		return new ResponseOk("Notifications sent.");
//	}
   
    @PutMapping("/respond/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public ResponseOk respondToNotification(@PathVariable("id") UUID notificationId, @RequestBody String response) {
        NotificationResponse response1 = NotificationResponse.valueOf(response);
    	notificationService.respondToNotification(notificationId, response1);
        return new ResponseOk("ok");
    }
}

