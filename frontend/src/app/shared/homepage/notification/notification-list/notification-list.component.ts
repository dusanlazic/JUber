import { Component, OnInit } from '@angular/core';
import { NotificationItemComponent } from './notification-item/notification-item.component';

@Component({
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.sass']
})


export class NotificationListComponent implements OnInit {

  driverArrivedNotification: NotificationItem = new DriverArrivedNotification("20");

  rideAcceptedNotification: NotificationItem = {
    backgroundColorClass: 'has-background-success',
    dataIcon: 'fluent:checkmark-12-filled',
    message: 'Your driver has arrived!'
  }

  rideRejectedNotification: NotificationItem = {
    backgroundColorClass: 'has-background-danger',
    dataIcon: 'fluent:emoji-sad-16-regular',
    message: 'Ride rejected! There are no available drivers right now. Try again later, or schedule a ride for later.'
  }

  constructor() { }

  ngOnInit(): void {
  }
}

interface NotificationItem{
  // isUnreadClass: string // 'is-unread' / ''
  backgroundColorClass: string,
  dataIcon: string,
  message: string
}

class DriverArrivedNotification implements NotificationItem {
    backgroundColorClass: string= 'has-background-info';
    dataIcon: string = 'mdi:car-side';
    message: string = '';

    constructor(arrivalTimeInMinutes: string) {
      this.message = `Ride accepted! Your driver will be there in about <b>${{arrivalTimeInMinutes}} minutes</b>.</p>`;
    }
};

