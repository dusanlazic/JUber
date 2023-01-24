import { Component, OnInit, ViewChild } from '@angular/core';;
import { DriverService } from 'src/services/driver/driver.service';
import { RideReview } from 'src/models/rideReview';
import { PastRidesResponse } from 'src/app/shared/profile-page/profile-navigation/past-rides/past-rides.component';
import { ActivatedRoute } from '@angular/router';
import { DriverInfo } from 'src/models/driver';
import { environment } from 'src/environments/environment';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-driver-info',
  templateUrl: './driver-info.component.html',
  styleUrls: ['./driver-info.component.sass']
})
export class DriverInfoComponent implements  OnInit {

  rides: Array<PastRidesResponse> = new Array<PastRidesResponse>()
  driverId: string = ''
  driverInfo!: DriverInfo

  URL_BASE: string = environment.API_BASE_URL;

  constructor(
    private driverService: DriverService,
    private route: ActivatedRoute,
    public authService: AuthService
  ) {}

  ngOnInit() {
    this.getDriverIdFromUrl();
    this.getDriverRides();
    this.getDriverDetails();
  }

  private getDriverIdFromUrl(): void {
    let id = this.route.snapshot.paramMap.get('driverId');
    if(id){
      this.driverId = id;
    }
  }

  private getDriverDetails() : void {
    this.driverService.getDriversInfo(this.driverId).subscribe({
      next: (res: DriverInfo) => {
        this.driverInfo = res;
      }
    })
  }

  private getDriverRides(): void {
    this.driverService.getDriversPastRides(this.driverId).subscribe({
      next: (res: Array<PastRidesResponse>) => {
        this.rides = res;
      }
    })
  }

  clickedRideRow(event: any) : void{
    console.log(event)
  }

  clickedReviewRow(row: RideReview) : void{
    console.log(row)
  }

  fullName(){
    return `${this.driverInfo.profile.firstName} ${this.driverInfo.profile.lastName}`
  }
}
