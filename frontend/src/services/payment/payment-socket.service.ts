import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { PaymentWebsocketshareService } from './payment-websocketshare.service';
import { AuthService } from '../auth/auth.service';
import { LoggedUser } from 'src/models/user';

@Injectable({
    providedIn: 'root'
})
export class PaymentWebSocketAPI {
    webSocketEndPoint: string = environment.API_SOCKET_URL;
    topic: string = "/user/queue/balance";
    stompClient: any;
    loggedUser: LoggedUser | undefined;

    constructor(private websocketShare: PaymentWebsocketshareService, private authService: AuthService) {
        authService.getNewLoggedUser().subscribe((user) => {
            if (user && user.email != this.loggedUser?.email) {
                console.log(user);
                this.loggedUser = user;
                this.connect();
            }
            else {
                this.loggedUser = undefined;
                this.disconnect();
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