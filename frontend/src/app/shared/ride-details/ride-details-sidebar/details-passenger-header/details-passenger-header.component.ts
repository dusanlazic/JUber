import { Component, Input, OnInit } from '@angular/core';
import { FullRide } from 'src/models/ride';
import { LoggedUser } from 'src/models/user';
import { RideService } from 'src/services/ride/ride.service';

@Component({
  selector: 'app-details-passenger-header',
  templateUrl: './details-passenger-header.component.html',
  styleUrls: ['./details-passenger-header.component.sass']
})
export class DetailsPassengerHeaderComponent implements OnInit {

  @Input() ride!: FullRide;
  @Input() loggedUser!: LoggedUser;

  abandon: boolean = false;
  reason: string = '';

  constructor(private rideService: RideService) { }

  ngOnInit(): void {
  }

  abandonRide() {    
    this.rideService.abandonRide(this.ride.id, this.reason).subscribe(response => {
      console.log(response);
      window.location.reload();
    });
  }

  panic() {
    this.rideService.panic(this.ride.id).subscribe(response => {
      console.log(response);
      window.location.reload();
    }
    );
  }

}
