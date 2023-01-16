import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { AuthService } from 'src/services/auth/auth.service';
import { UserService } from 'src/services/user.service';
import { PassengerMapComponent } from 'src/app/passenger/passenger-map/passenger-map.component';
import { PassengerSidebarComponent } from 'src/app/passenger/passenger-sidebar/passenger-sidebar.component';
import { Place, Ride } from 'src/models/ride';
import { Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { LoggedUser, Roles } from 'src/models/user';
import { DriverService } from 'src/services/driver/driver.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  loggedUser!: LoggedUser;
  ride: Ride | undefined;
  URL_BASE: string = environment.API_BASE_URL;
  DEFAULT_PROFILE_PHOTO: string = environment.DEFAULT_PROFILE_PHOTO;


  constructor(
    private authService: AuthService,
    private driverService: DriverService,
    private router: Router,
    private store: Store<{state: AppState}>
  ) { 
    this.store.select('state').subscribe(state => {
      this.ride = state.ride
    })
    // this.ride.places = [ new Place("Dr Ivana Ribara 13, Novi Sad", "via Blaba")]
    // this.ride.places.push(new Place("Narodnog fronta 57, Novi Sad", "via Narodnog Fronta"))
  }

  ngOnInit(): void {
      this.authService.getCurrentUser().subscribe({
        next: (user) => {
          this.loggedUser = user;
          console.log(this.loggedUser);
          
        }
      });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}