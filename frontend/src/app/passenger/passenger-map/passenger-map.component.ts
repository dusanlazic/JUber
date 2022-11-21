import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import  * as L from 'leaflet';
import { filter, lastValueFrom, Observable, Subscription } from 'rxjs';
import { Point } from 'src/models/map';
import { Place, Ride, Route } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';
import { NominatimService } from 'src/services/map/nominatim.service';
import { HttpRequestService } from 'src/services/util/http-request.service';
import 'leaflet-routing-machine';
import { State, StateObservable, Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { PreviewRouteSelectedAction } from 'src/app/store/ride.actions';

@Component({
  selector: 'passenger-map',
  templateUrl: './passenger-map.component.html',
  styleUrls: ['./passenger-map.component.sass']
})
export class PassengerMapComponent implements AfterViewInit {

  	private map!: L.Map;

  	// ride!: Observable<AppState>;

  	center: L.LatLng = new L.LatLng(45.2671, 19.8335)
  	zoom: number = 13

	controls: L.Control[] = []
	lines: L.Polyline[] = []


  	mapOptions = {
		show: false,
		routeWhileDragging: false,
		showAlternatives: true,
		lineOptions: {
			styles: [{color: 'red', weight: 4}],
			addWaypoints: false,
			extendToWaypoints: true,
			missingRouteTolerance: 0
		},
		altLineOptions: {
			styles: [{color: 'blue', opacity: 0.5, weight: 3}],
			extendToWaypoints: true,
			missingRouteTolerance: 0
		},
	}

	private initMap(): void {
		this.map = L.map('map', {
		center: this.center,
		zoom: this.zoom
		});

		const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 18,
		minZoom: 3,
		});

		tiles.addTo(this.map);
	}

  	constructor(private mapService: MapService, 
			  private httpService: HttpRequestService, 
			  private nominatimService: NominatimService,
			  private store: Store<{state: AppState}>) {
   }

	ngAfterViewInit(): void {
		this.initMap();
		let ride = this.store.select('state');
		ride.subscribe(state => {
			this.clearMap()
			console.log("Alooo");
			console.log(state);
			
			if(state === undefined) return;
			if(state.previewPlace !== null) {
				this.drawPreview(state.ride, state.previewPlace);
			}
			this.drawRide(state.ride)
		})

	}

	drawPreview(ride: Ride, place: Place) {
		for (const route of place.routes) {
			if (route.selected === true) {
				this.drawPolyline(route.coordinates)
			} else {
				this.drawPolyline(route.coordinates, 'blue', 2, true, route, place)
			}
		}
		this.drawMarker(place.point!)
	}

	drawRide(ride: Ride) {

		if (ride.places.length > 1) {
			let i = 1
			this.drawMarker(ride.places[0].point!)
			for (; i < ride.places.length; i++) {
				let curr = ride.places[i];
				if(curr.editing) continue;
				let selectedRoute = curr.routes.filter(x => x.selected).at(0)
				this.drawPolyline(selectedRoute!.coordinates);
				this.drawMarker(curr.point!)
			}
		} else if(ride.places.length == 1) {
			this.drawMarker(ride.places[0].point!)
		}
	}


	clearMap() {
		this.center = this.map.getCenter();
		this.zoom = this.map.getZoom();
		this.map = this.map.remove();
		this.initMap();
	}


	drawMarker(point: Point) {
		L.marker([point.latitude, point.longitude]).addTo(this.map);
	}

	drawPolyline(points: Array<Point>, color = 'red', weight = 4, selectable=false, route?: Route, place?: Place) {
		let leafletPts: Array<[number, number]> = [];

		for (const point of points) {
			leafletPts.push([point.latitude, point.longitude])
		}
		
		let polyline = new L.Polyline(leafletPts, {
			color: color,
			weight: weight,
		});

		this.lines.push(polyline);

		if (selectable && route && place) {
			polyline.on('click', (event) => {
				this.store.dispatch(PreviewRouteSelectedAction({route: route}))
			})
		}
		
		polyline.addTo(this.map);

	}
}
