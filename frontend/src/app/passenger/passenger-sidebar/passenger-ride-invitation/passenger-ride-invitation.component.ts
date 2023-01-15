import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { IRideRequest } from 'src/app/store/rideRequest/rideRequest';
import { environment } from 'src/environments/environment';
import { AuthService } from 'src/services/auth/auth.service';
import { RideSocketShareService } from 'src/services/ride/ridesocketshare.service';
import { RideWebSocketAPI } from 'src/services/ride/ride-message.service';
import { HttpRequestService } from 'src/services/util/http-request.service';
import { WebsocketshareService } from 'src/services/notification/websocketshare.service';

@Component({
  selector: 'app-passenger-ride-invitation',
  templateUrl: './passenger-ride-invitation.component.html',
  styleUrls: ['./passenger-ride-invitation.component.sass']
})
export class PassengerRideInvitationComponent implements OnInit {

  pals: any[] = [];
  palsStatus: string[] = [];

  convert = {
    "ACCEPTED": "Ready",
    "PENDING": "Waiting...",
    "DENIED": "Denied",
    "Ready": "Ready",
    "Denied": "Denied",
    "Waiting...": "Waiting...",
  }

  constructor(private authService:AuthService, 
              private store: Store<{rideRequest: IRideRequest}>,
              private websocketService: RideSocketShareService,
              private httpRequestService: HttpRequestService) {

    this.store.select('rideRequest').subscribe(state => {
			if(state === undefined) return;
      authService.getCurrentUser().subscribe(x => {
        this.pals = [];
        this.pals.push(x);
        this.pals.push(...state.passengersInfo);
        this.palsStatus.push('Ready');
        state.passengersInfo.forEach(element => {
          this.palsStatus.push('Waiting...');
        });
      })
		})

    

    websocketService.getNewValue().subscribe((resp: any) => {
      console.log(resp);
      console.log(this.pals);
      let data = JSON.parse(resp);
      let i = 0;
      for(let pal of this.pals) {
        console.log(pal.email, data);
        
        if(pal.email === data.email) {
          this.palsStatus[i] = this.convert[data.status as keyof typeof this.convert];
        }
        i++;
      }
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
