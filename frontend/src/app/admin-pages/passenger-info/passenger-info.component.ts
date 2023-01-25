import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PastRidesResponse } from 'src/app/shared/profile-page/profile-navigation/past-rides/past-rides.component';
import { environment } from 'src/environments/environment';
import { PersonDTO } from 'src/models/user';
import { PassengerService } from 'src/services/passenger/passenger.service';
import { AuthService } from 'src/services/auth/auth.service';

enum ActiveTable {
  PAST_RIDES='PAST_RIDES',
  REVIEWS='REVIEWS'
}
@Component({
  selector: 'app-passenger-info',
  templateUrl: './passenger-info.component.html',
  styleUrls: ['./passenger-info.component.sass']
})
export class PassengerInfoComponent implements OnInit {

  rides: Array<PastRidesResponse> = new Array<PastRidesResponse>()

  activeTable:string = ActiveTable.PAST_RIDES;

  passengerId: string = ''
  passengerInfo!: PersonDTO

  URL_BASE: string = environment.API_BASE_URL;

  constructor(
    private passengerService: PassengerService,
    private route: ActivatedRoute,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.getPassengerIdFromUrl();
    this.getPassengerRides();
    this.getPassengerDetails();
  }

  private getPassengerIdFromUrl(): void {
    let id = this.route.snapshot.paramMap.get('passengerId');
    if(id){
      this.passengerId = id;
    }
  }

  private getPassengerDetails() : void {
    this.passengerService.getPassengerInfo(this.passengerId).subscribe({
      next: (res: PersonDTO) => {
        this.passengerInfo = res;
      }
    })
  }

  private getPassengerRides(): void {
    this.passengerService.getPassengerPastRides(this.passengerId).subscribe({
      next: (res: Array<PastRidesResponse>) => {
        this.rides = res;
      }
    })
  }

  clickedRideRow(event: any) : void{
    // this.router.navigate([event.id])
  }

  fullName(){
    return `${this.passengerInfo?.firstName} ${this.passengerInfo?.lastName}`
  }
  
}
