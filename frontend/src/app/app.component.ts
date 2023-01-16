import { Component } from '@angular/core';
import { NotificationWebSocketAPI } from 'src/services/notification/notification-socket.service';
import { RideWebSocketAPI } from 'src/services/ride/ride-message.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'frontend';

  constructor(private notificationWebSocketAPI: NotificationWebSocketAPI, private rideWebSocketAPI: RideWebSocketAPI) {
    notificationWebSocketAPI.connect();
    rideWebSocketAPI.connect();
  }
}
