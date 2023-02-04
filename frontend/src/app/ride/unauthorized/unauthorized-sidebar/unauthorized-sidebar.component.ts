import { AfterViewChecked, AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { IRideRequest } from 'src/app/store/rideRequest/rideRequest';
import { Ride, Route } from 'src/models/ride';
import { RideRequest } from 'src/models/rideRequest';
import { MapService } from '../../services/map/map.service';
import { RideService } from 'src/services/ride/ride.service';
import { Router } from '@angular/router'
import * as _ from 'lodash';
import { decode, encode } from "@googlemaps/polyline-codec";
import { IPoint } from 'src/app/store/ride';

@Component({
	selector: 'unauthorized-sidebar',
	templateUrl: './unauthorized-sidebar.component.html',
	styleUrls: ['./unauthorized-sidebar.component.sass']
})
export class UnathorizedSidebarComponent implements OnInit, AfterViewInit {

	ride: Ride | undefined;

	isEditing: boolean = false;
	editingInd: number = -1;

	private rideRequest: RideRequest = new RideRequest();
	price: number = 0;
	totalDuration: number = 0
	totalDistance: number = 0
	
	constructor(
		private mapService: MapService, 
		private rideService: RideService,
		private store: Store<{state: AppState}>,
		private rideRequestStore: Store<{rideRequest: IRideRequest}>,
		public router: Router
	) {
		this.store.select('state').subscribe(state => {
			this.ride = state.ride
			this.calculatePrice();
		}) 

		this.rideRequestStore.select('rideRequest').subscribe(state => {
			this.rideRequest = state
		})
	}

	ngAfterViewInit() : void {
		this.rideRequestStore.select('rideRequest').subscribe(state => {
			this.rideRequest = state
			// TODO: samo za izmenu vozila i  voznje
			this.calculatePrice()
		})
	}

	ngOnInit(): void {
		this.mapService.editing$().subscribe(placeInd => {
			
			if(placeInd === -1) return;
			if(placeInd === -2) this.isEditing = false;

			if(this.isEditing && this.editingInd === placeInd) {
				this.isEditing = false;
			}
			else {
				this.isEditing = true;
			}
			this.editingInd = placeInd;
		})
	}

	sendRequest() : void {
		console.info("ovo je send request...")

		let rideRequest = {...this.rideRequest}
		rideRequest.passengerEmails = rideRequest.passengersInfo.map((pal)=> pal.email)
		let ride: any = _.cloneDeepWith(this.ride)!;
		let totalDuration = 0;
		let totalDistance = 0;

		for(let place of ride.places) {
			place.id = Number.NaN;
			for(let route of place.routes) {
				let coords: any = route.coordinates.map((x: { latitude: any; longitude: any; }) => [x.latitude, x.longitude])
				route.coordinatesEncoded = encode(coords);
				if(route.selected) {
					totalDuration += route.duration;
					totalDistance += route.distance;
				}
			}
			place.latitude = place.point.latitude;
			place.longitude = place.point.longitude;
		}
		rideRequest.ride = ride;
		this.calculatePrice();
		rideRequest.ride.fare = this.price;
		rideRequest.ride.duration = totalDuration;
		rideRequest.ride.distance = totalDistance;
		console.log(this.rideRequest)
		
		this.rideService.sendRideRequest(rideRequest).subscribe({
			next: () => {
				console.log("request sent")
				this.router.navigate(['/ride'])
			},
			error: (e) => {
				console.log(e)
			}
		})
		
	}


	private calculatePrice() : void {
		let sumPrice = 0
		const vehicleType = this.rideRequest?.additionalRequests.vehicleType
		if(vehicleType !== undefined && vehicleType !== null){
			sumPrice += vehicleType.price;
		}
		let duration = 0;
		let distance = 0;

		if(this.ride) {
			
			let totalDistance: number = 0;
			this.ride.places.forEach(place => {
				let selected = place.routes.filter(route => route.selected).at(0)
				totalDistance += selected?.distance ? selected.distance : 0;
				duration += selected?.duration ? selected.duration : 0;
				distance += selected?.distance ? selected.distance : 0;
			});
			sumPrice += Math.ceil(totalDistance / 1000 * 120);
		}
		this.totalDistance = distance;
		this.totalDuration = duration;
		this.price = sumPrice
	}

	public ceil(num: number) : number {
		return Math.ceil(num)
	}


	private extractFullRouteCoordinates(ride: Ride) : IPoint[] {
		let coords: IPoint[] = []

		for(let place of ride.places) {
			for(let route of place.routes) {
				if(route.selected) {
					coords.push(...route.coordinates)
				}
			}
		}

		return coords;
	}


}
