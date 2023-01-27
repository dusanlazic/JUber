import { Component, Input, OnInit } from '@angular/core';
import { Place, Ride, Route } from 'src/models/ride';
import { MapService } from 'src/app/ride/services/map/map.service';
import { OSRM, IOsrmWaypoint } from 'osrm-rest-client';
import { decode, encode } from "@googlemaps/polyline-codec";
import { AppState } from 'src/app/store/ride.reducer';
import { Store } from '@ngrx/store';
import { DeletePlaceAction, PreviewRouteSelectedAction, RemovePreviewAction, SetPreviewAction, StopEditingAction, UpdateEditedPlace, UpdateRoutes } from 'src/app/store/ride.actions';
import { RoutingService } from 'src/app/ride/services/map/routing.service';
import * as _ from 'lodash';



@Component({
	selector: 'app-edit-place',
	templateUrl: './edit-place.component.html',
	styleUrls: ['./edit-place.component.sass']
})
export class EditPlaceComponent implements OnInit {

	name: string = ""
	origName: string = ""
	place: Place | undefined;
	prevPlace: Place | undefined;
	nextPlace: Place | undefined;
	routes: Array<Route> = [];
	osrm = OSRM();
	ind: number = -1
	origPlace: Place | undefined;
	origNextPlace: Place | undefined;
	was = false;

	constructor(private mapService: MapService, private routingService: RoutingService, private store: Store<{state: AppState}>) { }

	ngOnInit(): void {
		if(!this.was) {
			this.store.select('state').subscribe(x => {
				if(!this.was)
					for (const [ind, xplace] of x.ride.places.entries()) {
							if(xplace.editing) {
								this.place = xplace;
								this.prevPlace = x.ride.places.at(ind - 1)
								this.nextPlace = x.ride.places.at(ind + 1)
								this.name = this.place.name
								this.origName = this.place.name
								this.routes = this.place.routes
								this.ind = ind
								this.origPlace = _.cloneDeep(this.place);
								this.origNextPlace = _.cloneDeep(this.nextPlace);	
								console.log(xplace); 
							}
					}
			})
			this.was = true;
		}
	}

	routeSelected(route: Route) {
		this.store.dispatch(PreviewRouteSelectedAction({route: route}))
	}

	async preview() {
		if(this.place === undefined) return;
		this.place = await this.mapService.createPlaceByName(this.name, this.ind)
		this.store.dispatch(UpdateEditedPlace({place: this.place!}))
		if(this.nextPlace === undefined) return;
		this.routingService.getRoutes([this.place!.point!, this.nextPlace!.point!]).then((x: any) => {
			this.store.dispatch(UpdateRoutes({place: this.nextPlace!, routes: x}))
		})

	}

	async save() {
		if(this.origName !== this.name) 
			await this.preview()
		this.store.dispatch(RemovePreviewAction())
		this.store.dispatch(StopEditingAction())
		this.mapService.setEditing(this.ind);
		this.was = false;
	}

	async cancel() {
		console.log('this thing happens here');
		console.log(this.origPlace);
		console.log('should be removed i guess');
		
		if(!this.origPlace) return;
		this.store.dispatch(UpdateEditedPlace({place: this.origPlace}))
		if(this.origNextPlace)
			this.store.dispatch(UpdateRoutes({place: this.nextPlace!, routes: this.origNextPlace!.routes}))

		this.store.dispatch(StopEditingAction())
		this.store.dispatch(RemovePreviewAction())
		this.mapService.setEditing(this.ind);
		this.was = false;
	}


	delete() {
		let deleteFromStore =  () => {
			this.store.dispatch(RemovePreviewAction())
			this.store.dispatch(DeletePlaceAction({place: this.place!}))
			this.mapService.setEditing(this.ind);
		}

		if(this.ind !== 0) {

			if (this.nextPlace !== undefined) {
				this.routingService.getRoutes([this.prevPlace!.point!, this.nextPlace!.point!]).then((x: any) => {
					this.store.dispatch(UpdateRoutes({place: this.nextPlace!, routes: x}))
					deleteFromStore()
				})
			} else {
				deleteFromStore()
			}
			
		}
		else {
			if (this.nextPlace !== undefined) { 
				this.store.dispatch(UpdateRoutes({place: this.nextPlace!, routes: []}))
			}
			deleteFromStore()
		}
	}

}
