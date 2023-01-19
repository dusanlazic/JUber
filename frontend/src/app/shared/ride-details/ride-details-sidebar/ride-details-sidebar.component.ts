import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { lowerFirst } from 'lodash';
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
export class RideDetailsSidebarComponent implements OnInit, OnChanges {
  reload() {
    window.location.reload();
  }

  @Input() ride: FullRide | undefined;
  loggedUser!: LoggedUser;
  rideInProgress: boolean = true;
  isFavourite: number = -1;

  constructor(public authService: AuthService,
              private httpService: HttpRequestService,) { }
  
  
  
  ngOnChanges(changes: SimpleChanges): void {
    if(!this.ride) return;
    this.checkFavourite();
    if(this.ride.rideStatus === 'DENIED' || this.ride.rideStatus === 'FINISHED') {
      this.rideInProgress = false;
    }
  }

  checkFavourite() {
    this.httpService.get(environment.API_BASE_URL + '/ride/is-favourite/' + this.ride?.id).subscribe(response => {
      console.log(response);
      
      this.isFavourite = response.favourite ? 1 : 0;
    })
  }

  toggleFavourite() {
    console.log("TOGGLE FAVOURITE");
    
    this.httpService.put(environment.API_BASE_URL + '/ride/favourite/' + this.ride?.id, {}).subscribe(response => {
      this.checkFavourite();
    });
  }


  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        console.log("OVO JE USER ALOOO: " + user.id + " " + user.role);
        
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

  startRide() {
    if(!this.ride) return;
    this.httpService.put(environment.API_BASE_URL + '/simulation/start-ride/' + this.ride.id, {}).subscribe(response => {
      console.log(response);
      window.location.reload();
    })
  }

  endRide() {
    if(!this.ride) return;
    this.httpService.put(environment.API_BASE_URL + '/simulation/end-ride/' + this.ride.id, {}).subscribe(response => {
      console.log(response);
      window.location.reload();
    })
  }



}
