import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DriverArrivedNotification, Notification, NotificationStatus, NotificationType, TransferredNotification } from 'src/models/notification';
import { NotificationService } from 'src/services/notification/notification.service';
import { WebsocketshareService } from 'src/services/notification/websocketshare.service';
import { HttpRequestService } from 'src/services/util/http-request.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.sass']
})
export class NotificationComponent implements OnInit {

  showNotifications: boolean;
  unreadCount: number;
  
  notifications: Notification[] = [];

  constructor(
    private notificationService: NotificationService,
    private websocketService: WebsocketshareService,
  ) { 
    this.showNotifications = false;
    this.unreadCount = 0;
  }

  ngOnInit(): void {
      this.getNotifications();
      this.subscribeToNewNotifications();
  }

  private getNotifications(){
    this.notificationService.getAllNotifications().subscribe({
      next: (notifs: Array<Notification>) => {
        this.notifications = notifs;
        this.unreadCount = notifs.filter((notif) => notif.notificationStatus === NotificationStatus.UNREAD).length;
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  private subscribeToNewNotifications() : void {
    this.websocketService.getNewValue().subscribe({
      next: (res: string) => {
        if(res){
          this.unreadCount += 1;
          const notif = JSON.parse(res) as Notification;
          this.notifications.splice(0, 0, notif);
          this.notifications = [...this.notifications];
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  nofificationClick(): void {
    if(!this.showNotifications){

      if(this.unreadCount > 0){
        this.markAllAsRead();
        this.unreadCount = 0;
      }
    }
    else{
      this.notifications.map(x => x.notificationStatus=NotificationStatus.READ)
      this.notifications = [...this.notifications];
    }

    this.showNotifications = !this.showNotifications;
  }

  private markAllAsRead() : void {
    this.notificationService.markAllAsRead().subscribe({
      next: (res: any) => {
        console.log(res)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }


  // private sendNewNotification(): void {
  //   this.notificationService.sendNotification().subscribe({
  //     next: (res: any) => {
  //       console.log(res)
  //     },
  //     error: (res: HttpErrorResponse) => {
  //       console.log(res)
  //     },
  //   })
  // }

}