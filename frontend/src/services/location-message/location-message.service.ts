import { Injectable } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environments/environment';
import { LoggedUser } from 'src/models/user';
import { AuthService } from '../auth/auth.service';
import { RideSocketShareService } from '../ride/ridesocketshare.service';
import { LocationSocketShareService } from './locationshare.service';

@Injectable({
  providedIn: 'root'
})
export class LocationSocketService {

  webSocketEndPoint: string = environment.API_SOCKET_URL;
    topic: string = "/topic/locations";
    stompClient: any;
    loggedUser: LoggedUser | undefined;

    constructor(private websocketShare: LocationSocketShareService, private authService: AuthService){
        console.log("LMAOOOOO");
        
        authService.getNewLoggedUser().subscribe((user) => {
            console.log(user);
            
            if (user && user.email != this.loggedUser?.email) {
                this.loggedUser = user;
                this.connect();
                console.log("HELLO");
                
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
        this.stompClient.reconnect_delay = 1000;
        this.stompClient.connect({}, (frame: any) => {
            
            this.stompClient.subscribe(this.topic, (sdkEvent: any) => {
                this.onMessageReceived(sdkEvent);
            });
            //_this.stompClient.reconnect_delay = 2000;
        }, () => this.errorCallBack);
    };

    disconnect() {
        if (this.stompClient && this.stompClient.connected) {
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
        console.log("Message Recieved from Server :: " + message);
        
        this.websocketShare.onNewValueReceive(message.body);
    }

    send(message: any) {
        this.stompClient.send()
    }
}
