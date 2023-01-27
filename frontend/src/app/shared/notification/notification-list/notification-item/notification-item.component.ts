import { Component, OnInit, Input } from '@angular/core';
import { DriverArrivedNotification, EveryoneAcceptedRideNotification, NewRideAssignedNotification, Notification, NotificationItemProperties, NotificationStatus, NotificationType, RideCancelledNotification, RideReminderNotification, RideStatusUpdatedNotification } from 'src/models/notification';
import { RideStatus } from 'src/models/ride';
import { NotificationTemplate, NotificationTimestampUtil } from 'src/services/util/notification-template.service';


@Component({
  selector: 'app-notification-item',
  templateUrl: './notification-item.component.html',
  styleUrls: ['./notification-item.component.sass']
})
export class NotificationItemComponent implements OnInit {

  @Input()
  notification!: Notification;

  notificationTimestamp: string;
  properties: NotificationItemProperties;

  constructor() {
    this.notificationTimestamp = '';
    this.properties= { iconClass: '', icon: '', message: '', url: ''};
  }

  ngOnInit(): void {
    this.setNotificationProperties();
    this.notificationTimestamp = NotificationTimestampUtil.getTimestampText(this.notification.date);
  }

  private setNotificationProperties(): void{
    switch (this.notification.type) {

      case NotificationType.DRIVER_ARRIVED: {
        const notificationCast = this.notification as DriverArrivedNotification
        this.properties = NotificationTemplate.driverArived(notificationCast.driverName)
        break;
      }
      case NotificationType.RIDE_ASSIGNED: {
        const notificationCast = this.notification as NewRideAssignedNotification
        this.properties = NotificationTemplate.rideAssigned(notificationCast.startLocationName, notificationCast.rideId)
        break;
      }

      // STAVITI U NOTIFIKACIJE SA SLIKOM? 
      case NotificationType.RIDE_CANCELLED: {
        const notificationCast = this.notification as RideCancelledNotification
        this.properties = NotificationTemplate.rideCancelled(notificationCast.canceler)
        break;
      }

      case NotificationType.RIDE_REMINDER: {
        const notificationCast = this.notification as RideReminderNotification
        this.properties = NotificationTemplate.rideReminder(notificationCast)
        break;
      }

      case NotificationType.RIDE_STATUS_UPDATED: {
        const notificationCast = this.notification as RideStatusUpdatedNotification
        if (notificationCast.status === RideStatus.DENIED){
          this.properties = NotificationTemplate.rideRejected()
        }
        else {
          this.properties = NotificationTemplate.rideStatusUpdated(notificationCast.rideId)
        }
        break;
      }

      case NotificationType.RIDE_ACCEPTED: {
        const notificationCast = this.notification as EveryoneAcceptedRideNotification;
        this.properties = NotificationTemplate.rideAccepted(notificationCast.untilDriverArival) 
        break;
      }

      default:
        break;
    }

  }

}
