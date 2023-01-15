import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { DriverState } from 'src/app/store/driverRide/driver-ride.reducer';
import { Ride } from 'src/models/ride';

@Component({
  selector: 'driver-sidebar',
  templateUrl: './driver-sidebar.component.html',
  styleUrls: ['./driver-sidebar.component.sass']
})
export class DriverSidebarComponent implements OnInit {

  ride: Ride | null = null;

  constructor(private store: Store<{driverRide: DriverState}>) {
    this.store.select('driverRide').subscribe((state: DriverState) => {
      this.ride = state.ride;
    })
   }

  ngOnInit(): void {
  }

}
