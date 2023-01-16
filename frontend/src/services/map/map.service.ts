import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { last } from 'lodash';
import { BehaviorSubject, firstValueFrom, Observable } from 'rxjs';
import { AppState } from 'src/app/store/ride.reducer';
import { Point } from 'src/models/map';
import { Place, Route } from 'src/models/ride';
import { NominatimService } from './nominatim.service';
import { RoutingService } from './routing.service';

@Injectable({
	providedIn: 'root'
})
export class MapService {

	state: AppState | undefined;

	constructor(private routingService: RoutingService,
		private nominatimService: NominatimService,
		private store: Store<{state: AppState}>) { 
			this.store.select('state').subscribe(state => {
				this.state = state;
			})
	}

	colors = [
		"#F94144",
		"#43AA8B",
		"#F8961E",
		"#577590",
		"#F9C74F",
		"#90BE6D",
		"#F3722C",
		"#4D908E",
		"#F9844A",
		"#277DA1",
	]

	//
	private _mapPreview = new BehaviorSubject<string>('');

	mapPreview$(): Observable<any> {
		return this._mapPreview.asObservable();
	}

	setPreviewLocation(location: string): void {
		this._mapPreview.next(location);
	}
	
	//
	private _editing = new BehaviorSubject<number>(-1);

	editing$(): Observable<any> {
		return this._editing.asObservable();
	}

	setEditing(yes: number): void {
		this._editing.next(yes);
	}

	//

	async createPlaceByName(name: string, ind = -1): Promise<any> {
		let place = new Place();
		place.name = name;
		place.editing = true;
		place.id = ind == -1 ? this.state!.ride.places.length : ind;
		let geoLoc = (await firstValueFrom(this.nominatimService.addressLookup(name)))[0];
		place.point = new Point(geoLoc.lat, geoLoc.lon);
		let lastPlace;
		if(ind === -1 && lastPlace === undefined) {  
			lastPlace = (await firstValueFrom(this.store.select('state'))).ride.places.at(-1);
		}
		else {
			lastPlace = (await firstValueFrom(this.store.select('state'))).ride.places.at(ind - 1);
		}

		let routes: Route[] = [];
		if (lastPlace !== undefined) {
			routes = (await this.routingService.getRoutes([lastPlace.point!, place.point]));
			routes[0].selected = true;
			place.option = routes[0].name;
		}
		place.routes = routes;
		return place;
	}
	

}