import { Component, Input, OnInit } from '@angular/core';
import { FullRide } from 'src/models/ride';
import { LoggedUser } from 'src/models/user';
import { RideService } from 'src/services/ride/ride.service';

@Component({
  selector: 'app-details-driver-header',
  templateUrl: './details-driver-header.component.html',
  styleUrls: ['./details-driver-header.component.sass']
})
export class DetailsDriverHeaderComponent implements OnInit {

  @Input() ride!: FullRide;
  @Input() loggedUser!: LoggedUser;

  abandon: boolean = false;
  reason: string = '';



  constructor(private rideService: RideService) { }

  ngOnInit(): void {
  }

  toggleAbandonRideReason() {
    this.abandon = !this.abandon;
  }

  acceptRide() {
    this.rideService.acceptRide(this.ride.id).subscribe(response => {
      console.log(response);
      window.location.reload();
    });
  }

  declineRide() {
    if(!this.ride) return;
    this.rideService.declineRide(this.ride.id).subscribe(response => {
      console.log(response);
      window.location.reload();
    });
  }

  startRide() {
    this.rideService.startRide(this.ride.id).subscribe(response => {
      console.log(response);
      window.location.reload();
    });
  }

  abandonRide() {    
    this.rideService.abandonRide(this.ride.id, this.reason).subscribe(response => {
      console.log(response);
      window.location.reload();
    });
  }

  endRide() {
    this.rideService.endRide(this.ride.id).subscribe(response => {
      console.log(response);
      window.location.reload();
    });
  }

}
