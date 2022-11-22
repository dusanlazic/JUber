import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from 'src/app/store/ride.reducer';
import { Ride } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';

@Component({
	selector: 'passenger-sidebar',
	templateUrl: './passenger-sidebar.component.html',
	styleUrls: ['./passenger-sidebar.component.sass']
})
export class PassengerSidebarComponent implements OnInit {

	ride: Ride | undefined;

	isEditing: boolean = false;
	editingInd: number = -1;
	
	constructor(private mapService: MapService, private store: Store<{state: AppState}>) {
		this.store.select('state').subscribe(state => {
			this.ride = state.ride
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

}
