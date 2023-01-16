import { Component } from '@angular/core';
import { NotificationWebSocketAPI } from 'src/services/notification/notification-socket.service';
import { SupportAdminWebSocketAPI } from 'src/services/support/support-admin-socket.service';
import { SupportChatWebSocketAPI } from 'src/services/support/support-chat-socket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'frontend';

  constructor(
    private notificationWebSocketAPI: NotificationWebSocketAPI,
    private supportAdminWebSocketAPI: SupportAdminWebSocketAPI,
    private supportChatWebSocketAPI: SupportChatWebSocketAPI
  ) {
    notificationWebSocketAPI.connect();
    supportAdminWebSocketAPI.connect();
    supportChatWebSocketAPI.connect();
  }
}
