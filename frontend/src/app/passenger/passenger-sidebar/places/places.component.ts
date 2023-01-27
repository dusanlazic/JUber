import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AddPlaceAction, OptimizeRoutesAction } from 'src/app/store/ride.actions';
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
  showOptimize: boolean = false;

  constructor(private mapService: MapService, private store: Store<{state: AppState}>) { 
  }

  ngOnInit(): void {
	this.store.select('state').subscribe(state => {
		this.ride = state.ride;
    this.checkCanBeOptimized();
	})
  }

  toggleModal(): void {
	this.isModalActive = !this.isModalActive;
  }

  updateEmptyPlace(evt: any) {
	  this.toggleModal();
  }

  checkCanBeOptimized() {
    let changed = false;
    if (!this.ride || this.ride.places.length < 2) {
      this.showOptimize = false;
      return;
    }
    this.ride.places.forEach(place => {
      if (!place.routes || place.routes.length < 2) return;
      if (!place.routes[0].selected) {
        this.showOptimize = true;
        changed = true;
      }
    })
    if (!changed) this.showOptimize = false;
  }

  optimize() {
    this.store.dispatch(OptimizeRoutesAction());
  }

}
