import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Notification, NotificationResponse, TransferredNotification } from 'src/models/notification';
import { HttpRequestService } from '../util/http-request.service';
import { WebsocketshareService } from './websocketshare.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(
    private httpRequestService: HttpRequestService
  ){}

  getAllNotifications(): Observable<Array<Notification>> {
      const url = environment.API_BASE_URL + '/notifications/';

      return this.httpRequestService.get(url) as Observable<Array<Notification>>;
  }

  markAllAsRead(): Observable<any> {
    const url = environment.API_BASE_URL + '/notifications/read-all';

    return this.httpRequestService.patch(url, null) as Observable<any>;
  }

  sendNotification(): Observable<any> {
    const url = environment.API_BASE_URL + '/notifications/send';

    return this.httpRequestService.post(url, null) as Observable<any>;
  }

  respondToNotification(notificationId: string, response: NotificationResponse) : Observable<any> {
    const url = environment.API_BASE_URL + `/notifications/respond/${notificationId}`;
    return this.httpRequestService.put(url, response) as Observable<any>;
  }

}
