import { Component, OnInit, ViewChild } from '@angular/core';;
import { DriverService } from 'src/services/driver/driver.service';
import { RideReview } from 'src/models/rideReview';
import { PastRidesResponse } from 'src/app/profile/profile-navigation/past-rides/past-rides.component';
import { ActivatedRoute } from '@angular/router';
import { DriverInfo } from 'src/models/driver';
import { environment } from 'src/environments/environment';
import { AuthService } from 'src/services/auth/auth.service';

enum ActiveTable {
  PAST_RIDES='PAST_RIDES',
  REVIEWS='REVIEWS'
}

@Component({
  selector: 'app-driver-info',
  templateUrl: './driver-info.component.html',
  styleUrls: ['./driver-info.component.sass']
})
export class DriverInfoComponent implements  OnInit {

  rides: Array<PastRidesResponse> = new Array<PastRidesResponse>()
  reviews: Array<RideReview> = new Array<RideReview>()

  activeTable:string = ActiveTable.PAST_RIDES;

  driverId: string = ''
  driverInfo!: DriverInfo

  driverAvgRating: number = 0
  vehicleAvgRating: number = 0

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
    this.getDriverReviews();
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

  private getDriverReviews() : void {
    this.driverService.getDriversReviews(this.driverId).subscribe({
      next: (res: Array<RideReview>) => {
        this.reviews = res;
        this.calculateAvgRatings();
      }
    })
  }

  private calculateAvgRatings(): void {
    const reviewsCount = this.reviews.length
    if(reviewsCount > 0){
      const driverRatingSum = this.reviews.reduce((accumulator, review: RideReview) => {
        return accumulator + review.driverRating;
      }, 0);
      const vehicleRatingSum = this.reviews.reduce((accumulator, review: RideReview) => {
        return accumulator + review.vehicleRating;
      }, 0);
      
      this.driverAvgRating = driverRatingSum / reviewsCount
      this.vehicleAvgRating = vehicleRatingSum / reviewsCount
    }
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

  setActiveTable(activeTable: string){
    this.activeTable = activeTable;
  }
}
