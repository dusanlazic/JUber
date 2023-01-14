import { Component, OnInit, Input } from '@angular/core';
import { Notification, NotificationItemProperties, NotificationResponse, RideInvitationNotification } from 'src/models/notification';
import { NotificationTimestampUtil } from 'src/services/util/notification-template.service';

@Component({
  selector: 'app-ride-invite',
  templateUrl: './ride-invite.component.html',
  styleUrls: ['./ride-invite.component.sass']
})
export class RideInviteComponent implements OnInit {

  @Input()
  notification!: Notification;
  notificationCast!: RideInvitationNotification;
  message: string
  notificationTimestamp: string;

  constructor(
    // private httpRequestService: HttpRequestService
  ) {
    this.message = ''
    this.notificationTimestamp=''
   }

  ngOnInit(): void {
    this.notificationCast = this.notification as RideInvitationNotification;
    this.message = `User 
      <strong>${this.notificationCast.inviterName}</strong>
      has invited you to a ride to 
      <strong>${this.notificationCast.startLocationName}</strong>.`;
    this.notificationTimestamp = NotificationTimestampUtil.getTimestampText(this.notificationCast.date);
  }

  acceptRide() {
    this.notificationCast.response = NotificationResponse.ACCEPTED;
       console.log("acceptRide");
  }
  declineRide() {
    this.notificationCast.response = NotificationResponse.DECLINED;
    console.log("declineRide");
}

}
