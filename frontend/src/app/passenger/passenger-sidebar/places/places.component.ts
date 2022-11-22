import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AddPlaceAction } from 'src/app/store/ride.actions';
import { AppState } from 'src/app/store/ride.reducer';
import { Place, Ride } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';

@Component({
  selector: 'app-places',
  templateUrl: './places.component.html',
  styleUrls: ['./places.component.sass']
})
export class PlacesComponent implements OnInit {
  isModalActive: boolean = false;
  ride: Ride | undefined;

  constructor(private mapService: MapService, private store: Store<{state: AppState}>) { 
  }

  ngOnInit(): void {
	this.store.select('state').subscribe(state => {
		this.ride = state.ride;
		console.log(this.ride);
	})
  }

  toggleModal(): void {
	this.isModalActive = !this.isModalActive;
  }

  updateEmptyPlace(evt: any) {
	this.toggleModal();
  }

}
