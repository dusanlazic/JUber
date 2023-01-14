import { Component, OnInit } from '@angular/core';
import { DriverArrivedNotification, Notification, NotificationType, TransferredNotification } from 'src/models/notification';
import { NotificationService } from 'src/services/notification/notification.service';
import { WebsocketshareService } from 'src/services/notification/websocketshare.service';
import { HttpRequestService } from 'src/services/util/http-request.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.sass']
})
export class NotificationComponent implements OnInit {

  isNotificationOpen: boolean;
  notificationCount: number;
  
  notifications: Notification[] = [];

  constructor(
    private notificationService: NotificationService,
    private websocketService: WebsocketshareService,
  ) { 
    this.isNotificationOpen = false;
    this.notificationCount = 1;
  }

  ngOnInit(): void {
      this.getNotifications();
      this.subscribeToNewNotifications();
  }

  private getNotifications(){
    this.notificationService.getAllNotifications().subscribe({
      next: (res: Array<Notification>) => {
        res.forEach(element => {
          if(element.type === NotificationType.DRIVER_ARRIVED){
            element = element as DriverArrivedNotification
            console.log(element.driverName)
          }
        });
        this.notifications = res;
      },
      error: (res: any) => {
        console.log(res)
      },
    })
  }

  private subscribeToNewNotifications() : void {
    this.websocketService.getNewValue().subscribe({
      next: (res: Notification) => {
        // const obj = JSON.parse(res);
        this.notifications.push(res)
        this.notifications = [...this.notifications];
      },
      error: (res: any) => {
        console.log(res)
      },
    })
  }

  nofificationClick(): void {
    // this.sendNewNotification();
    if(!this.isNotificationOpen){
      this.notificationCount = 0;
    }
    this.isNotificationOpen = !this.isNotificationOpen;
  }


  sendNewNotification(): void {
    this.notificationService.sendNotification().subscribe({
      next: (res: any) => {
        console.log(res)
      },
      error: (res: any) => {
        console.log(res)
      },
    })
  }

}
