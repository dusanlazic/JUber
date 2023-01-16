import { Component } from '@angular/core';
import { NotificationWebSocketAPI } from 'src/services/notification/notification-socket.service';
import { AdminSupportWebSocketAPI } from 'src/services/support/admin/admin-chat/admin-support-socket.service';
import { AdminConversationWebSocketAPI } from 'src/services/support/admin/admin-conversations/admin-conversation-socket.service';
import { SupportChatWebSocketAPI } from 'src/services/support/user/support-chat-socket.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'frontend';

  constructor(
    private notificationWebSocketAPI: NotificationWebSocketAPI,
    private supportChatWebSocketAPI: SupportChatWebSocketAPI,
    private adminSupportWebSocketAPI: AdminSupportWebSocketAPI,
    private adminConversationWebSocketAPI: AdminConversationWebSocketAPI,
  ) {
    notificationWebSocketAPI.connect();
    supportChatWebSocketAPI.connect();
    adminSupportWebSocketAPI.connect();
    adminConversationWebSocketAPI.connect();
  }
}
