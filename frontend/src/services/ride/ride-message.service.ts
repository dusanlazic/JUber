import { Injectable } from '@angular/core';
import { RideSocketShareService } from './ridesocketshare.service';
import { environment } from 'src/environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable()
export class RideWebSocketAPI {
    webSocketEndPoint: string = environment.API_SOCKET_URL;
    topic: string = "/user/queue/ride";
    stompClient: any;
    
    constructor(private websocketShare: RideSocketShareService){
         
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