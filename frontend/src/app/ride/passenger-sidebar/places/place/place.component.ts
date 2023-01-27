import { state } from '@angular/animations';
import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { after } from 'lodash';
import { MoveToPreviewAction, StopEditingAction, SwapPlaceDownAction, SwapPlaceUpAction } from 'src/app/store/ride.actions';
import { AppState } from 'src/app/store/ride.reducer';
import { Place, Route } from 'src/models/ride';
import { MapService } from 'src/app/ride/services/map/map.service';
import { NominatimService } from 'src/app/ride/services/map/nominatim.service';
import { RoutingService } from 'src/app/ride/services/map/routing.service';

@Component({
	selector: 'app-place',
	templateUrl: './place.component.html',
	styleUrls: ['./place.component.sass']
})
export class PlaceComponent implements OnInit {

	@Input() place: Place | undefined;
	@Input() index: number = -1;
	state: AppState | undefined;

	color: string = 'gray';

	constructor(private mapService: MapService, private store: Store<{state: AppState}>, private routingService: RoutingService) {
		
	 }

	ngOnInit(): void {
		this.store.select('state').subscribe(state => {
			this.state = state;			
			this.color = this.mapService.colors.at(this.index) || 'gray';
		});
	}

	editPlace() {
		if(!this.place) return;
		this.store.dispatch(StopEditingAction());
		this.store.dispatch(MoveToPreviewAction({place: this.place}))
		this.mapService.setEditing(this.index);
	}

	async swapUp() {
        if(!this.place || !this.state  || this.index === 0) return;
		let beforeRoutes: Route[], afterRoutes : Route[];

		if (this.index === 1) {
			beforeRoutes = [];
		} else {
			beforeRoutes = await this.routingService.getRoutes([this.state?.ride.places[this.index - 2].point!, this.state?.ride.places[this.index].point!]);
		}

		if (this.index === this.state?.ride.places.length - 1) {
			afterRoutes = [];
		} else {
			afterRoutes = await this.routingService.getRoutes([this.state?.ride.places[this.index - 1].point!, this.state?.ride.places[this.index + 1].point!]);
		}
		
		this.store.dispatch(SwapPlaceUpAction({id: this.place.id, beforeRoutes: beforeRoutes, afterRoutes: afterRoutes}))
		
	}

	async swapDown() {
		if(!this.place || !this.state || this.index === this.state?.ride.places.length - 1) return;

		let beforeRoutes: Route[], afterRoutes : Route[];

		if (this.index === 0) {
			beforeRoutes = [];
		} else {
			beforeRoutes = await this.routingService.getRoutes([this.state?.ride.places[this.index - 1].point!, this.state?.ride.places[this.index + 1].point!]);
		}

		if (this.index === this.state?.ride.places.length - 2) {
			afterRoutes = [];
		} else {
			afterRoutes = await this.routingService.getRoutes([this.state?.ride.places[this.index].point!, this.state?.ride.places[this.index + 2].point!]);
		}

		this.store.dispatch(SwapPlaceDownAction({id: this.place.id, beforeRoutes: beforeRoutes, afterRoutes: afterRoutes}))
    }

}
