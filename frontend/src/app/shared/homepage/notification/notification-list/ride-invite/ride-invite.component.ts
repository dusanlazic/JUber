import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit, Input } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Notification, NotificationItemProperties, NotificationResponse, RideInvitationNotification } from 'src/models/notification';
import { ApiResponse } from 'src/models/responses';
import { AuthService } from 'src/services/auth/auth.service';
import { NotificationService } from 'src/services/notification/notification.service';
import { RideService } from 'src/services/ride/ride.service';
import { NotificationTimestampUtil } from 'src/services/util/notification-template.service';
import { Toastr } from 'src/services/util/toastr.service';

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
  URL_BASE: string = environment.API_BASE_URL

  constructor(
    private rideService: RideService,
    private notificationService: NotificationService,
    private toastrService: Toastr,
    public authService: AuthService
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
    this.rideService.acceptRide(this.notificationCast.rideId).subscribe({
      next: ()=> {
        this.respondToNotification(NotificationResponse.ACCEPTED);
      },
      error: (err: HttpErrorResponse) => {
        this.handleAcceptRideFail(err.error);
      }
    })
  }

  declineRide() {
    this.rideService.declineRide(this.notificationCast.rideId).subscribe({
      next: ()=> {
        this.respondToNotification(NotificationResponse.DECLINED);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }

  private respondToNotification(response: NotificationResponse) : void {
    this.notificationService.respondToNotification(this.notificationCast.notificationId, response).subscribe({
      next: () => {
        this.notificationCast.response = response;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }

  private handleAcceptRideFail(error: ApiResponse) {
    if(error.status === HttpStatusCode.BadRequest){
      this.toastrService.error('Please try again later.', error.message);
    }
    else{
      this.toastrService.error('Please try again later.', 'Ride acceptance failed!');
    }
  }

}
