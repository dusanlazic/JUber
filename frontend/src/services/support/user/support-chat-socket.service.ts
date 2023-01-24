import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { SupportChatWebsocketshareService } from './support-chat-websocketshare.service';
import { AuthService } from 'src/services/auth/auth.service';
import { LoggedUser } from 'src/models/user';

@Injectable({
  providedIn: 'root'
})
export class SupportChatWebSocketAPI {
  webSocketEndPoint: string = environment.API_SOCKET_URL;
  topic: string = "/user/queue/support/chat";
  stompClient: any;
  loggedUser: LoggedUser | undefined;

  constructor(private websocketShare: SupportChatWebsocketshareService, private authService: AuthService){
    authService.getNewLoggedUser().subscribe((user) => {
        console.log(user);
        
        if (user && user.email != this.loggedUser?.email) {
            this.loggedUser = user;
            this.connect();
            console.log('Connected to ride socket!');
            
        }
        else {
            this.loggedUser = undefined;
            // this.disconnect();
        }
    })
  }
  connect() {
      console.log("Initialize WebSocket Connection");
      let ws = new SockJS(this.webSocketEndPoint);
      this.stompClient = Stomp.over(ws);
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
      console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error: any) {
      console.log("errorCallBack -> " + error)
      setTimeout(() => {
          this.connect();
      }, 5000);
  }  

  onMessageReceived(message: any) {    
      this.websocketShare.onNewValueReceive(message.body);
  }

  send(message: any) {
      this.stompClient.send()
  }
}
