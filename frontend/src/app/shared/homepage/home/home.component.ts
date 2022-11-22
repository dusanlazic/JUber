import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { AuthService } from 'src/services/auth/auth.service';
import { UserService } from 'src/services/user.service';
import { PassengerMapComponent } from 'src/app/passenger/passenger-map/passenger-map.component';
import { PassengerSidebarComponent } from 'src/app/passenger/passenger-sidebar/passenger-sidebar.component';
import { Place, Ride } from 'src/models/ride';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements AfterViewInit {

  loggedUser: any;
  ride: Ride;
  

  constructor(
    private authService: AuthService,
    private router: Router,
  ) { 
    this.ride = new Ride()
    // this.ride.places = [ new Place("Dr Ivana Ribara 13, Novi Sad", "via Blaba")]
    // this.ride.places.push(new Place("Narodnog fronta 57, Novi Sad", "via Narodnog Fronta"))
  }

  ngAfterViewInit(): void {
      this.authService.getCurrentUser().subscribe({
        next: (user) => {
          this.loggedUser = user;
          console.log(this.loggedUser);
          
        }
      });
  }


}