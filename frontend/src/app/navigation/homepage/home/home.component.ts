import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { AuthService } from 'src/services/auth/auth.service';
import { UserService } from 'src/services/user.service';
import { PassengerMapComponent } from 'src/app/ride/passenger-map/passenger-map.component';
import { PassengerSidebarComponent } from 'src/app/ride/passenger-sidebar/passenger-sidebar.component';
import { Place, Ride } from 'src/models/ride';
import { Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { LoggedUser, Roles } from 'src/models/user';
import { DriverService } from 'src/services/driver/driver.service';
import { environment } from 'src/environments/environment';
import { HttpRequestService } from 'src/services/util/http-request.service';
import { SetRideAction } from 'src/app/store/ride.actions';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {

  loggedUser: LoggedUser | undefined;
  ride: Ride | undefined;
  URL_BASE: string = environment.API_BASE_URL;
  sub: Subscription | undefined;
  GOOGLE_AUTH_URL= environment.GOOGLE_AUTH_URL_LOGIN;
  FACEBOOK_AUTH_URL= environment.FACEBOOK_AUTH_URL_LOGIN;

  constructor(
    public authService: AuthService,
    private driverService: DriverService,
    private router: Router,
    private route: ActivatedRoute,
    private httpService: HttpRequestService,
    private store: Store<{state: AppState}>
  ) { 
    this.store.select('state').subscribe(state => {
      this.ride = state.ride
    })
    
    

    // this.ride.places = [ new Place("Dr Ivana Ribara 13, Novi Sad", "via Blaba")]
    // this.ride.places.push(new Place("Narodnog fronta 57, Novi Sad", "via Narodnog Fronta"))
  }


  ngOnDestroy() {
    this.sub?.unsubscribe();
  }

  ngOnInit(): void {
      this.authService.getCurrentUser().subscribe({
        next: (user) => {
          this.loggedUser = user;
          console.log(this.loggedUser); 
        },
        error: (err) => {
          this.loggedUser = undefined;
        }

      });
  }


  logout(): void {
    this.authService.logout();
  }

}
