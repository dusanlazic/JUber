import { Injectable } from '@angular/core';
import { WebsocketshareService } from './websocketshare.service';
import { environment } from 'src/environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationWebSocketAPI {
  webSocketEndPoint: string = environment.API_SOCKET_URL;
  topic: string = "/user/queue/notifications";
  stompClient: any;

  constructor(private websocketShare: WebsocketshareService, private authService: AuthService) {
    authService.getNewValue().subscribe((user) => {
        if (user) {
            console.log(user);
            this.connect();
        }
        else {
            this.disconnect();
        }
    })
  }

  connect() {
      console.log("Initialize WebSocket Connection");
      let ws = new SockJS(this.webSocketEndPoint);

      this.stompClient = Stomp.over(ws);
      this.stompClient.reconnect_delay = 1000;
      this.stompClient.connect({}, (frame: any) => {
          this.stompClient.subscribe(this.topic, (sdkEvent: any) => {
            this.onMessageReceived(sdkEvent);
          });
          //_this.stompClient.reconnect_delay = 2000;
      }, () => this.errorCallBack);
  };

  disconnect() {
      if (this.stompClient !== null) {
          this.stompClient.disconnect();
      }
      alert('DISCONNECT')
      console.log("Disconnected");
  }

  errorCallBack(error: any) {
      alert("Error: " + error);
      console.log("errorCallBack -> " + error)
      setTimeout(() => {
          this.connect();
      }, 5000);
  }  

  onMessageReceived(message: any) {
      alert(message)
      this.websocketShare.onNewValueReceive(message.body);
  }

  send(message: any) {
      this.stompClient.send()
  }
}
