import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import  * as L from 'leaflet';
import { lastValueFrom } from 'rxjs';
import { Point } from 'src/models/map';
import { Place, Ride } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';
import { NominatimService } from 'src/services/map/nominatim.service';
import { HttpRequestService } from 'src/services/util/http-request.service';
import 'leaflet-routing-machine';

@Component({
  selector: 'passenger-map',
  templateUrl: './passenger-map.component.html',
  styleUrls: ['./passenger-map.component.sass']
})
export class PassengerMapComponent implements AfterViewInit {

  private map!: L.Map;

  @Input() ride: Ride;

  ppc: Map<[Place, Place], L.Control> = new Map()
  placeRouteOption: Map<string, any> = new Map()
  fakeControl: L.Control | null = null;

  center: L.LatLng = new L.LatLng(45.2671, 19.8335)
  zoom: number = 13


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

	this.drawPaths();

  }

  constructor(private mapService: MapService, 
			  private httpService: HttpRequestService, 
			  private nominatimService: NominatimService) {
	this.ride = new Ride();
   }

  ngAfterViewInit(): void {
	this.initMap();
	this.mapService.mapPreview$().subscribe((location: string) => {
		if(this.fakeControl)
			this.map.removeControl(this.fakeControl);
		this.showPreview(location);
	})

	this.mapService.confirmedLocation$().subscribe(async (location: string) => {
		console.log("CALLED CONFIRMED");
		let filtered = this.ride.places.filter(x => !x.editing)
		let lastPlace = filtered[filtered.length - 1]
		if(this.ride.places.length === 1) {
			let place = this.ride.places[0];
			place.editing = false;
			place.option = "";
			await this.setPoint(place)
			return;
		}
		for (const place of this.ride.places) {
			if(place.editing) {
				place.editing = false
				let mem = this.placeRouteOption.get(place.name)
				if(mem === undefined) {
					place.editing = true;
					await this.setPoint(place);
					this.createMem(lastPlace, place);
					return;
				}
				place.option = mem.option
				this.ride.routeNames.push(place.option)
				this.ride.routes.push(mem.coordinates)
				this.placeRouteOption.delete(place.name)
				this.ppc.set([lastPlace, place], this.fakeControl!)
				this.fakeControl = null;
				await this.setPoint(place)
				this.clearMap();
			}
		}
	})

	this.mapService.redraw$().subscribe((yes: boolean) => {
			if(yes) {
				this.clearMap();
			}
		})
	}


	createMem(placeA: Place, placeB: Place) {
		let cnt = L.Routing.control({
			show: false,
			routeWhileDragging: false,
			showAlternatives: true,
			waypoints: [
				L.latLng(placeA.point!.latitude, placeA.point!.longitude),
				L.latLng(placeB.point!.latitude, placeB.point!.longitude),
			],
			lineOptions: {
				styles: [{color: 'red', weight: 4}],
				extendToWaypoints: true,
				missingRouteTolerance: 0
			},
			altLineOptions: {
				styles: [{color: 'blue', opacity: 0.5, weight: 3}],
				extendToWaypoints: true,
				missingRouteTolerance: 0
			},
		})
		.addTo(this.map)
		.on('routeselected', (e: any) => {
				console.log(e);
				let coords = e.route.coordinates.map((x: any) => new Point(x.lat, x.lng));
				let mem = {option: e.route.name, coordinates: coords};
				placeB.option = mem.option;
				console.log(mem);
				this.placeRouteOption.set(placeB.name, {option: e.route.name, coordinates: coords});
				this.mapService.setConfirmedLocation(placeB.name);
				console.log("THIS IS PLACEB", placeB);
				
			}
		)
		this.fakeControl = cnt;
	}

	showPreview(location: string) {
		if(!location) return;
		this.nominatimService.addressLookup(location).subscribe((x: any[]) => {
			let longitude = x[0].lon;
			let latitude = x[0].lat;
			console.log(longitude, latitude);
			let previewPoint: Point = new Point(latitude, longitude)
			let previewPlace: Place = new Place(location, "", previewPoint)
			if(this.ride.places.length === 0) {
				L.marker([latitude, longitude]).addTo(this.map);
			} else {
				let lastPoint = this.ride.places[this.ride.places.length - 1];
				let fakeCnt = this.drawPath(lastPoint!, previewPlace, true);
				this.fakeControl = fakeCnt;
			}
		})
  }

	clearMap() {
		this.center = this.map.getCenter();
		this.zoom = this.map.getZoom();
		this.map = this.map.remove();
		this.initMap();
	}

  drawPath(placeA: Place, placeB: Place, alt: boolean=true) {

	console.log(alt);
	let cnt = L.Routing.control({
		show: false,
		routeWhileDragging: false,
		waypoints: [
			L.latLng(placeA.point!.latitude, placeA.point!.longitude),
			L.latLng(placeB.point!.latitude, placeB.point!.longitude),
		],
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
	  })
	  .addTo(this.map)
	  .on('routeselected', (e: any) => {
			console.log("THIS IS E: ", e);
			placeB.option = e.route.name;
			let bind = this.ride.places.findIndex((place, index, places) => place.name === placeB.name)
			let coords = e.route.coordinates.map((x: any) => new Point(x.lat, x.lng));
			if(bind !== -1) {
				this.ride.routeNames[bind] = e.route.name;
				this.ride.routes[bind] = coords
			} else {
				this.placeRouteOption.set(placeB.name, {option: e.route.name, coordinates: coords})
			}
		}
	)
	return cnt;
  }

  drawMarker(point: Point) {
	L.marker([point.latitude, point.longitude]).addTo(this.map);
  }

  async setPoint(place: Place) {
	let res = await lastValueFrom(this.nominatimService.addressLookup(place.name))
	console.log(res)
	place.point = new Point(res[0].lat, res[0].lon)
  }

  async drawPaths() {
	for (let place of this.ride.places) {
		console.log(place);
		console.log(place.point);
		if(!place.point) await this.setPoint(place);
	}
	if (this.ride.places.length > 1) {
		let i = 1
		for (; i < this.ride.places.length; i++) {
			let prev = this.ride.places[i-1];
			let curr = this.ride.places[i];
			let hasRoute = this.ride.routes.length > i && this.ride.routes[i].length !== 0
			if(!hasRoute) {
				let cont = this.drawPath(prev, curr)
				this.ppc.set([prev, curr], cont);
			} else {
				if(i == 1) this.drawMarker(this.ride.places[i - 1].point!)
				this.drawMarker(this.ride.places[i].point!)
				this.drawPolyline(this.ride.routes[i]);
			}
		}
	} else if(this.ride.places.length == 1) {
		this.drawMarker(this.ride.places[0].point!)
	}
  }

	drawPolyline(points: Array<Point>) {
		let leafletPts: Array<[number, number]> = [];

		for (const point of points) {
			leafletPts.push([point.latitude, point.longitude])
		}
		
		let polyline = new L.Polyline(leafletPts, {
			color: 'red',
			weight: 4,
		});
		
		polyline.addTo(this.map);
	}
}
