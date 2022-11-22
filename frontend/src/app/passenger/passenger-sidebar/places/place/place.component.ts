import { state } from '@angular/animations';
import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { MoveToPreviewAction, StopEditingAction } from 'src/app/store/ride.actions';
import { AppState } from 'src/app/store/ride.reducer';
import { Place } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';

@Component({
	selector: 'app-place',
	templateUrl: './place.component.html',
	styleUrls: ['./place.component.sass']
})
export class PlaceComponent implements OnInit {

	@Input() place: Place | undefined;
	@Input() index: number;

	constructor(private mapService: MapService, private store: Store<{state: AppState}>) {
		this.index = -1;
	 }

	ngOnInit(): void {

	}

	editPlace() {
		if(!this.place) return;
		this.store.dispatch(StopEditingAction());
		this.store.dispatch(MoveToPreviewAction({place: this.place}))
		this.mapService.setEditing(this.index);
	}

}
