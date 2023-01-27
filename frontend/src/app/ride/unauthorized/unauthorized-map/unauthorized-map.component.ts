import { AfterViewInit, Component, Input, OnDestroy, OnInit } from '@angular/core';
import  * as L from 'leaflet';
import { filter, lastValueFrom, Observable, Subscription } from 'rxjs';
import { Point } from 'src/models/map';
import { Place, Ride, Route } from 'src/models/ride';
import { MapService } from '../../services/map/map.service';
import { NominatimService } from '../../services/map/nominatim.service';
import { HttpRequestService } from 'src/services/util/http-request.service';
import 'leaflet-routing-machine';
import { State, StateObservable, Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { PreviewRouteSelectedAction } from 'src/app/store/ride.actions';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'unauthorized-map',
  templateUrl: './unauthorized-map.component.html',
  styleUrls: ['./unauthorized-map.component.sass']
})
export class UnathorizedMapComponent implements AfterViewInit, OnDestroy {

  	private map!: L.Map;

  	center: L.LatLng = new L.LatLng(45.2671, 19.8335)
  	zoom: number = 13
	controls: L.Control[] = []
	lines: L.Polyline[] = []
	myIcon = L.divIcon({className: 'my-div-icon'});
	interval!: NodeJS.Timer;

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
		this.map = L.map('mapica', {
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
			console.log(state);
			
			if(state === undefined) return;
			if(state.previewPlace !== null) {
				this.drawPreview(state.ride, state.previewPlace);
			}
			this.drawRide(state.ride)
		});
		this.interval = setInterval(() => { this.getAndDrawDrivers() }, 500);
	}

	ngOnDestroy(): void {
		clearInterval(this.interval);
		this.clearMap();
	}

	drawPreview(ride: Ride, place: Place) {
		for (const route of place.routes) {
			if (route.selected === true) {
				this.drawPolyline(route.coordinates)
			} else {
				this.drawPolyline(route.coordinates, 'blue', 2, true, route, place)
			}
		}
		this.drawMarker(place.point!, this.mapService.colors[0])
	}

	drawRide(ride: Ride) {

		if (ride.places.length > 1) {
			let i = 1
			this.drawMarker(ride.places[0].point!, this.mapService.colors[0])
			for (; i < ride.places.length; i++) {
				let curr = ride.places[i];
				if(curr.editing) continue;
				let selectedRoute = curr.routes.filter(x => x.selected).at(0)
				this.drawPolyline(selectedRoute!.coordinates, this.mapService.colors[i]);
				this.drawMarker(curr.point!, this.mapService.colors[i])
			}
		} else if(ride.places.length == 1) {
			this.drawMarker(ride.places[0].point!, this.mapService.colors[0])
		}
	}


	clearMap() {
		this.center = this.map.getCenter();
		this.zoom = this.map.getZoom();
		this.map = this.map.remove();
		this.initMap();
	}


	drawMarker(point: Point, color: string) {
		const markerHtmlStyles = `
			background-color: ${color};
			width: 2rem;
			height: 2rem;
			display: block;
			left: -1.5rem;
			top: -1.5rem;
			position: relative;
			border-radius: 2rem 2rem 0;
			transform: rotate(45deg);
			border: 1px solid #FFFFFF
		`

		const icon = L.divIcon({
			className: "my-custom-pin",
			iconAnchor: [0, 24],
			popupAnchor: [0, -36],
			html: `<span style="${markerHtmlStyles}" />`
		})
		L.marker([point.latitude, point.longitude], {icon: icon}).addTo(this.map);
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


	getAndDrawDrivers() {
		// this.httpService.get(environment.API_BASE_URL + '/accounts/drivers/all-locations').subscribe(
		// 	(data) => {
		// 		this.drawDrivers(data);
		// 	}
		// );
	}



	driverIcon = L.icon({
		iconUrl: '/assets/images/driver-pin.png',

		iconSize:     [38, 38], // size of the icon
		popupAnchor:  [-3, -3] // point from which the popup should open relative to the iconAnchor
	});


	driverMarkers: L.Marker[] = [];
	drawDrivers(data: any) {

		for (let driverMaker of this.driverMarkers) {
			this.map.removeLayer(driverMaker);
		}

		for(let driver of data) {
			let marker  = L.marker([driver.latitude, driver.longitude], {icon: this.driverIcon}).addTo(this.map);
			marker.bindPopup(driver.email);
			this.driverMarkers.push(marker);
		}
		
	}

}
