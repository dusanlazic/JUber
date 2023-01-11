import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { IRideRequest } from 'src/app/store/rideRequest/rideRequest';
import { Ride, Route } from 'src/models/ride';
import { RideRequest } from 'src/models/rideRequest';
import { MapService } from 'src/services/map/map.service';
import { RideService } from 'src/services/ride/ride.service';
import { Router } from '@angular/router'
import * as _ from 'lodash';
import { decode, encode } from "@googlemaps/polyline-codec";

@Component({
	selector: 'passenger-sidebar',
	templateUrl: './passenger-sidebar.component.html',
	styleUrls: ['./passenger-sidebar.component.sass']
})
export class PassengerSidebarComponent implements OnInit {

	ride: Ride | undefined;

	isEditing: boolean = false;
	editingInd: number = -1;

	private rideRequest: RideRequest = new RideRequest();
	price: number = 0;
	
	constructor(
		private mapService: MapService, 
		private rideService: RideService,
		private store: Store<{state: AppState}>,
		private rideRequestStore: Store<{rideRequest: IRideRequest}>,
		public router: Router
	) {
		this.store.select('state').subscribe(state => {
			this.ride = state.ride
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
		console.log(this.rideRequest)

		let rideRequest = {...this.rideRequest}
		rideRequest.passengerEmails = rideRequest.passengersInfo.map((pal)=> pal.email)
		let ride: any = _.cloneDeepWith(this.ride)!;

		for(let place of ride.places) {
			place.id = Number.NaN;
			for(let route of place.routes) {
				let coords: any = route.coordinates.map((x: { latitude: any; longitude: any; }) => [x.latitude, x.longitude])
				route.coordinates = encode(coords);
			}
		}
		rideRequest.ride = ride;
		

		this.rideService.sendRideRequest(rideRequest).subscribe({
			next: () => {
				console.log("request sent")
				this.router.navigate(['/invitation'])
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
		this.price = sumPrice
	}
}
