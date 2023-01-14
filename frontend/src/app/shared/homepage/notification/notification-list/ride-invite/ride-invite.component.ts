import { Component, OnInit } from '@angular/core';
import { WebsocketshareService } from 'src/services/notification/websocketshare.service';
import { HttpRequestService } from 'src/services/util/http-request.service';

@Component({
  selector: 'app-ride-invite',
  templateUrl: './ride-invite.component.html',
  styleUrls: ['./ride-invite.component.sass']
})
export class RideInviteComponent implements OnInit {

  constructor(
    private websocketService: WebsocketshareService,
    private httpRequestService: HttpRequestService) { }

  ngOnInit(): void {
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
  

}
