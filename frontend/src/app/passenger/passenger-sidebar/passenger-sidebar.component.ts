import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { IRideRequest } from 'src/app/store/rideRequest/rideRequest';
import { Ride } from 'src/models/ride';
import { RideRequest } from 'src/models/rideRequest';
import { MapService } from 'src/services/map/map.service';
import { RideService } from 'src/services/ride/ride.service';

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
		private rideRequestStore: Store<{rideRequest: IRideRequest}>
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
			console.log("MAP SERVICE SET EDITING");
			
			if(placeInd === -1) return;
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
		console.log(this.ride)

		this.rideService.sendRideRequest(this.rideRequest).subscribe({
			next: () => {
				console.log("request sent")
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
