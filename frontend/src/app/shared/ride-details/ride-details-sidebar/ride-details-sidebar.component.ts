import { Component, Input, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { FullRide } from 'src/models/ride';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import {  } from 'src/services/auth/auth.service';
import { HttpRequestService } from 'src/services/util/http-request.service';

@Component({
  selector: 'app-ride-details-sidebar',
  templateUrl: './ride-details-sidebar.component.html',
  styleUrls: ['./ride-details-sidebar.component.sass']
})
export class RideDetailsSidebarComponent implements OnInit {
  reload() {
    window.location.reload();
  }

  @Input() ride: FullRide | undefined;
  loggedUser!: LoggedUser;

  constructor(public authService: AuthService,
              private httpService: HttpRequestService,) { }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.loggedUser = user;
      }
    });
  }

  acceptRide() {
    if(!this.ride) return;
    this.httpService.put(environment.API_BASE_URL + '/ride/accept/' + this.ride.id, {}).subscribe(response => {
      console.log(response);
      window.location.reload();
    })
  }

  declineRide() {
    if(!this.ride) return;
    this.httpService.put(environment.API_BASE_URL + '/ride/decline/' + this.ride.id, {}).subscribe(response => {
      console.log(response);
      window.location.reload();
    })
  }



}
