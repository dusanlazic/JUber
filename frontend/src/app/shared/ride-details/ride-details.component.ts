import { Component, OnDestroy, OnInit } from '@angular/core';
import { throwToolbarMixedModesError } from '@angular/material/toolbar';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { FullRide, Ride } from 'src/models/ride';
import { LoggedUser, Roles } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { DriverService } from 'src/services/driver/driver.service';
import { RideSocketShareService } from 'src/services/ride/ridesocketshare.service';
import { HttpRequestService } from 'src/services/util/http-request.service';
import { decode, encode } from "@googlemaps/polyline-codec";
import { Point } from 'src/models/map';
import { Toast, ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';


interface SocketMessage {
    type: string;
    ride: FullRide;
}


@Component({
  selector: 'app-ride-details',
  templateUrl: './ride-details.component.html',
  styleUrls: ['./ride-details.component.sass']
})
export class RideDetailsComponent implements OnInit, OnDestroy {

  loggedUser!: LoggedUser;
  ride: FullRide | undefined;
  rideInProgress: boolean = true;
  socketSubscription: Subscription | undefined;
  URL_BASE = environment.API_BASE_URL;
  
    
  constructor(public authService: AuthService,
              private driverService: DriverService,
              private router: Router,
              private httpService: HttpRequestService,
              private websocketService: RideSocketShareService,
              private toastrService: ToastrService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {

    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.loggedUser = user;
        console.log(this.loggedUser);
        this.getRideDetails();
        this.setupSocekts();
      }
    });
  }

  ngOnDestroy(): void {
    this.socketSubscription?.unsubscribe();
    this.websocketService.onNewValueReceive('');
  }

  preprocessRide(ride: FullRide) {
    for(let [i, passenger] of ride.passengers.entries()) {
      passenger.status = ride.passengersReady[i];
    }
    ride.places.forEach(place => place.routes.forEach(route => route.coordinates = decode(route.coordinatesEncoded).map(x => new Point(x[0], x[1]))))
    ride.places.forEach((place: any) => place.point = new Point(place.latitude, place.longitude));
    
  }

  getRideDetails() {
    let id = this.route.snapshot.paramMap.get('rideId');


    this.httpService.get(environment.API_BASE_URL + '/ride/' + (id ? id : 'active')).subscribe((data) => {
        let ride = data as FullRide;
        if(!ride) {
          return;
        }
        this.preprocessRide(ride);
        this.ride = ride;
        console.log(ride);
      }
    );
  }

  setupSocekts() {
    this.socketSubscription = this.websocketService.getNewValue().subscribe((resp: any) => {
      let data = undefined;
      try {
        data = JSON.parse(resp) as SocketMessage;
      } catch (error) {
        return;
      }
      if(!data) return;
      this.preprocessRide(data.ride);
      this.ride = data.ride;
      if(data.type === 'PAL_UPDATE_STATUS') {
        this.palUpdateStatus(data);
      } else if(data.type === 'DRIVER_FOUND') {
        this.driverUpdateStatus(data);
      } else if(data.type === 'RIDE_FAILED_LATE') {
        this.rideFailedLate(data, 'RIDE_FAILED_LATE');
      }
    });
  }

  palUpdateStatus(data: SocketMessage) {
    this.toastrService.info('A pal has been updated');
    let ride = this.ride as FullRide;
    if (ride.rideStatus === 'WAIT') {
      this.toastrService.info('Looking for a driver...');
    }
  }

  driverUpdateStatus(data: SocketMessage) {
    console.log(this.loggedUser.role);
  
    if(this.loggedUser.role == 'ROLE_PASSENGER') {
      this.toastrService.success('A driver has been assigned and is heading towards your location!');
    } else {
      this.toastrService.success('New ride has been assigned to you!');
    }
  }

  rideFailedLate(data: SocketMessage, reason: string) {
    this.toastrService.error("The ride has failed because a pal was late!");
    // wait for 5 seconds and then redirect to home
    setTimeout(() => {
      this.router.navigate(['/home']);
      window.location.reload();
    }, 5000);
  }


  logout(): void {
    this.authService.logout();
  }

}
