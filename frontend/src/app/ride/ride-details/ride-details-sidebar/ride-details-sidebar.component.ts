import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { lowerFirst } from 'lodash';
import { environment } from 'src/environments/environment';
import { FullRide } from 'src/models/ride';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import {  } from 'src/services/auth/auth.service';
import { HttpRequestService } from 'src/services/util/http-request.service';
import { RoutingService } from '../../services/map/routing.service';

import { Observable, Subscription } from 'rxjs';
import { Point } from 'src/models/map';
import { LocationSocketShareService } from 'src/services/location-message/locationshare.service';
import { DriverLocation } from '../../passenger-map/passenger-map.component';


@Component({
  selector: 'app-ride-details-sidebar',
  templateUrl: './ride-details-sidebar.component.html',
  styleUrls: ['./ride-details-sidebar.component.sass']
})
export class RideDetailsSidebarComponent implements OnInit, OnChanges {


  reload() {
    window.location.reload();
  }

  time: number | undefined;

  @Input() ride: FullRide | undefined;
  loggedUser!: LoggedUser;
  rideInProgress: boolean = true;
  isFavourite: number = -1;
  URL_BASE: string = environment.API_BASE_URL;
  timer: NodeJS.Timer | undefined;


  constructor(public authService: AuthService,
              private httpService: HttpRequestService,
              private routingService: RoutingService,
              private driverLocationService: LocationSocketShareService
              )
  {

  }
  

  ngOnChanges(changes: SimpleChanges): void {
    if(!this.ride) return;

    this.authService.getCurrentUser().subscribe((user) =>{
      this.loggedUser = user;
      if(this.loggedUser.role === 'ROLE_PASSENGER') {
        this.checkFavourite();
      }
    });
    
    if(this.ride.rideStatus === 'DENIED' || this.ride.rideStatus === 'FINISHED') {
      this.rideInProgress = false;
    }

    if (this.ride.rideStatus === 'ACCEPTED' && !this.timer) {
      this.timer = setInterval(() => {
        this.calculateTime();
      }, 3000);
    } else {
      clearInterval(this.timer);
    }

  }


  calculateTime() {
    if(!this.ride || !this.ride.driver || this.ride.rideStatus != 'ACCEPTED') return;
    console.log("CALCULATE TIME");
    
    this.getDriverLocation()?.subscribe((response: any) => {
      console.log(response);
      let driverLatitude = response.latitude;
      let driverLongitude = response.longitude;
      let passengerLatitude = this.ride?.places[0].point!.latitude!;
      let passengerLongitude = this.ride?.places[0].point!.longitude!;
      console.log(driverLatitude, driverLongitude, passengerLatitude, passengerLongitude);
      
      let locs: Point[] = [ new Point(driverLatitude, driverLongitude), new Point(passengerLatitude, passengerLongitude) ] 
      this.routingService.getRoutes(locs).then((response: any) => {
        let calctime = response[0].duration;
        console.log("NEW TIME " + calctime + 's');        
        this.time = calctime;
      })
    });
  }


  checkFavourite() {
    this.httpService.get(environment.API_BASE_URL + '/ride/is-favourite/' + this.ride?.id).subscribe(response => {
      console.log(response);
      
      this.isFavourite = response.favourite ? 1 : 0;
    })
  }

  getDriverLocation() : Observable<any> | undefined{
    if(!this.ride || !this.ride.driver) return;
    return this.httpService.get(environment.API_BASE_URL + '/accounts/drivers/location/' + this.ride.driver.id);
  }

  toggleFavourite() {
    console.log("TOGGLE FAVOURITE");
    
    this.httpService.put(environment.API_BASE_URL + '/ride/favourite/' + this.ride?.id, {}).subscribe(response => {
      this.checkFavourite();
    });
  }

  subscription: Subscription | undefined;

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

  startRide() {
    if(!this.ride) return;
    this.httpService.put(environment.API_BASE_URL + '/simulation/start-ride/' + this.ride.id, {}).subscribe(response => {
      console.log(response);
      window.location.reload();
    })
  }

  



}
