import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { createAction, Store } from '@ngrx/store';
import { last } from 'lodash';
import { lastValueFrom } from 'rxjs';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';
import { AddPlaceAction, AddPreviewToPlacesAction, RemovePreviewAction, SetPreviewAction, StopEditingAction } from 'src/app/store/ride.actions';
import { AppState } from 'src/app/store/ride.reducer';
import { Point } from 'src/models/map';
import { Place, Route } from 'src/models/ride';
import { MapService } from 'src/app/ride/services/map/map.service';
import { NominatimService } from 'src/app/ride/services/map/nominatim.service';
import { RoutingService } from 'src/app/ride/services/map/routing.service';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-empty-place',
  templateUrl: './empty-place.component.html',
  styleUrls: ['./empty-place.component.sass']
})
export class EmptyPlaceComponent implements OnInit {

  	name: string = '';
	place: Place = new Place();
	changedName: boolean = false
	state: AppState | undefined
  
  	@Output('updateEvent')
 	change: EventEmitter<any> = new EventEmitter<any>();

  	constructor(private map: MapService,
				private routingService: RoutingService,
				private nominatimService: NominatimService,
				private toastr: Toastr,
				private store: Store<{state: AppState}>) { }

  	ngOnInit(): void {
		this.store.select('state').subscribe(state => {
			this.state = state;
		})
  	}

	async fillPlace() {
		if(!this.changedName) return;
		this.map.createPlaceByName(this.name)
				.then(res => {
					this.dispatchActions(res);
					return Promise.resolve();
				})
				.catch(err => {
					this.toastr.error("Could not find place. Make sure it is in Novi Sad!")
					return Promise.reject(err);
				});
	}

	dispatchActions(res: Place) {
		this.place = res;
		this.changedName = false;
		if(this.state?.previewPlace) {
			this.store.dispatch(AddPreviewToPlacesAction())
			this.store.dispatch(RemovePreviewAction())
		} else {
			this.store.dispatch(AddPlaceAction({place: this.place}))
		}
		this.store.dispatch(StopEditingAction())
		this.change.emit({confirmed: true, name: this.name})
	}

	confirm() {
		this.fillPlace().catch(err => {
			console.log(err);
		})
  	}

	cancel() {
		// delete from store
		this.store.dispatch(RemovePreviewAction())
		this.change.emit({confirmed: false, name: this.name})
	}

	preview() {
		this.fillPlace().then(res => {
			// set in store
			this.store.dispatch(SetPreviewAction({payload: this.place}))
		})
	}

	changed() {
		this.changedName = true
	}
}
