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

	constructor(private routingService: RoutingService,
		private nominatimService: NominatimService,
		private store: Store<{state: AppState}>) { }

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
