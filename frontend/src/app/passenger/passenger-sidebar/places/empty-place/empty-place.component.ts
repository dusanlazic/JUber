import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { createAction, Store } from '@ngrx/store';
import { last } from 'lodash';
import { lastValueFrom } from 'rxjs';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';
import { AddPlaceAction, AddPreviewToPlacesAction, RemovePreviewAction, SetPreviewAction, StopEditingAction } from 'src/app/store/ride.actions';
import { AppState } from 'src/app/store/ride.reducer';
import { Point } from 'src/models/map';
import { Place, Route } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';
import { NominatimService } from 'src/services/map/nominatim.service';
import { RoutingService } from 'src/services/map/routing.service';

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
				private store: Store<{state: AppState}>) { }

  	ngOnInit(): void {
		this.store.select('state').subscribe(state => {
			this.state = state;
		})
  	}

	async fillPlace() {
		if(!this.changedName) return;
		this.place = await this.map.createPlaceByName(this.name);
		this.changedName = false;
	}

	confirm() {
		this.fillPlace().then(res => {
			// set in store
			if(this.state?.previewPlace) {
				this.store.dispatch(AddPreviewToPlacesAction())
				this.store.dispatch(RemovePreviewAction())
			} else {
				this.store.dispatch(AddPlaceAction({place: this.place}))
			}
			this.store.dispatch(StopEditingAction())
			
		}).catch(err => {
			console.log(err);
		})
		this.change.emit({confirmed: true, name: this.name})
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
		this.map.setPreviewLocation(this.name);
	}

	changed() {
		this.changedName = true
	}
}
