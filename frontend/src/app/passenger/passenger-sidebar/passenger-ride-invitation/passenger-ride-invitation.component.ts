import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { IRideRequest } from 'src/app/store/rideRequest/rideRequest';
import { environment } from 'src/environments/environment';
import { AuthService } from 'src/services/auth/auth.service';
import { WebSocketShareService } from 'src/services/ride/websocketshare.service';
import { RideWebSocketAPI } from 'src/services/ride/ride-message.service';
import { HttpRequestService } from 'src/services/util/http-request.service';

@Component({
  selector: 'app-passenger-ride-invitation',
  templateUrl: './passenger-ride-invitation.component.html',
  styleUrls: ['./passenger-ride-invitation.component.sass']
})
export class PassengerRideInvitationComponent implements OnInit {

  pals: any = [];

  constructor(private authService:AuthService, 
              private store: Store<{rideRequest: IRideRequest}>,
              private websocketService: WebSocketShareService,
              private httpRequestService: HttpRequestService) {

    this.store.select('rideRequest').subscribe(state => {
			if(state === undefined) return;
      authService.getCurrentUser().subscribe(x => {
        this.pals.push(x);
        this.pals.push(...state.passengersInfo);
      })
		})

  }


  public stompClient: any;
  public msg: any = [];
  subscription: any;
  
  sendMessage(message: any) {
    this.httpRequestService.get('http://localhost:8080/greet-socket').subscribe(x => {
      console.log("GREET SOCKET RESPONSE");
      console.log(x);
    });
  }

  ngOnInit(): void {
  }

}
