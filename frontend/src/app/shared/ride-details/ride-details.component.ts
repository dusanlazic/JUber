import { Component, OnInit } from '@angular/core';
import { throwToolbarMixedModesError } from '@angular/material/toolbar';
import { Router } from '@angular/router';
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


interface SocketMessage {
    type: string;
    ride: FullRide;
}


@Component({
  selector: 'app-ride-details',
  templateUrl: './ride-details.component.html',
  styleUrls: ['./ride-details.component.sass']
})
export class RideDetailsComponent implements OnInit {

  loggedUser!: LoggedUser;
  ride: FullRide | undefined;

  
    
  constructor(public authService: AuthService,
              private driverService: DriverService,
              private router: Router,
              private httpService: HttpRequestService,
              private websocketService: RideSocketShareService,
              private toastrService: ToastrService) { }

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

  preprocessRide(ride: FullRide) {
    for(let [i, passenger] of ride.passengers.entries()) {
      passenger.status = ride.passengersReady[i];
    }
    ride.places.forEach(place => place.routes.forEach(route => route.coordinates = decode(route.coordinatesEncoded).map(x => new Point(x[0], x[1]))))
    ride.places.forEach((place: any) => place.point = new Point(place.latitude, place.longitude));
  }

  getRideDetails() {
    this.httpService.get(environment.API_BASE_URL + '/ride/active').subscribe((data) => {
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
    this.websocketService.getNewValue().subscribe((resp: any) => {
      let data = JSON.parse(resp) as SocketMessage;
      this.preprocessRide(data.ride);
      this.ride = data.ride;
      if(data.type === 'PAL_UPDATE_STATUS') {
        this.palUpdateStatus(data);
      } else if(data.type === 'DRIVER_FOUND') {
        this.driverUpdateStatus(data);
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
    this.toastrService.success('A driver has been assigned and is heading towards your location!');
  }


  logout(): void {

    if(this.loggedUser.role === Roles.DRIVER){
      this.driverService.inactivate(this.loggedUser.email);
    }
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
