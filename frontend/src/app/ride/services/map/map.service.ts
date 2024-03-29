import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { last } from 'lodash';
import { BehaviorSubject, firstValueFrom, Observable } from 'rxjs';
import { AppState } from 'src/app/store/ride.reducer';
import { Point } from 'src/models/map';
import { Place, Route } from 'src/models/ride';
import { NominatimService } from './nominatim.service';
import { RoutingService } from './routing.service';
import { IPoint } from 'src/app/store/ride';
import { Toastr } from 'src/services/util/toastr.service';

@Injectable({
	providedIn: 'root'
})
export class MapService {

	state: AppState | undefined;

	constructor(private routingService: RoutingService,
		private nominatimService: NominatimService,
		private toastr : Toastr,
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
	private _editing = new BehaviorSubject<number>(-1);

	editing$(): Observable<any> {
		return this._editing.asObservable();
	}

	setEditing(yes: number): void {
		this._editing.next(yes);
	}

	//

	balans_lat = 45.25232843778114
	balans_lon = 19.8375130497505

	async createPlaceByName(name: string, ind = -1): Promise<any> {
		let place = new Place();
		place.name = name;
		place.editing = true;
		place.id = ind == -1 ? this.state!.ride.places.length : ind;
		let geoLoc = (await firstValueFrom(this.nominatimService.addressLookup(name)))[0];
		if(this.getDistanceFromLatLonInKm(geoLoc.lat, geoLoc.lon, this.balans_lat, this.balans_lon) > 5) {
			this.toastr.error("Nije moguće dodati lokaciju koja je udaljena više od 5km od Balans palacinkarnice.");
			return Promise.reject();
		}
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

	getDistanceFromLatLonInKm(lat1: number, lon1: number, lat2: number, lon2: number) {
		var R = 6371;
		var dLat = this.deg2rad(lat2-lat1);
		var dLon = this.deg2rad(lon2-lon1); 
		var a = 
		  Math.sin(dLat/2) * Math.sin(dLat/2) +
		  Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * 
		  Math.sin(dLon/2) * Math.sin(dLon/2)
		  ; 
		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		var d = R * c;
		return d;
	}
	  
	deg2rad(deg: number) {
		return deg * (Math.PI/180)
	}

	cosineDistanceBetweenPoints(pointA: IPoint, pointB: IPoint) {
		let lat1: number = pointA.latitude
		let lon1: number = pointA.longitude
		let lat2: number = pointB.latitude
		let lon2: number = pointB.longitude
		const R = 6371e3;
		const p1 = lat1 * Math.PI/180;
		const p2 = lat2 * Math.PI/180;
		const deltaP = p2 - p1;
		const deltaLon = lon2 - lon1;
		const deltaLambda = (deltaLon * Math.PI) / 180;
		const a = Math.sin(deltaP/2) * Math.sin(deltaP/2) +
				  Math.cos(p1) * Math.cos(p2) *
				  Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
		const d = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)) * R;
		return d;
	}


	pathLength(points: IPoint[]) {
		let total = 0;
		for(let i = 1; i < points.length; i++) {
			total += this.cosineDistanceBetweenPoints(points[i - 1], points[i]);
		}
		return total;
	}
	

}
