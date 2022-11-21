import { Component, Input, OnInit } from '@angular/core';
import { Place, Ride, Route } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';
import { OSRM, IOsrmWaypoint } from 'osrm-rest-client';
import { decode, encode } from "@googlemaps/polyline-codec";
import { AppState } from 'src/app/store/ride.reducer';
import { Store } from '@ngrx/store';
import { PreviewRouteSelectedAction, UpdateEditedPlace, UpdateRoutes } from 'src/app/store/ride.actions';
import { RoutingService } from 'src/services/map/routing.service';


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

  constructor(private mapService: MapService, private routingService: RoutingService, private store: Store<{state: AppState}>) { }

  ngOnInit(): void {
    this.store.select('state').subscribe(x => {
      for (const [ind, xplace] of x.ride.places.entries()) {
          if(xplace.editing) {
            this.place = xplace;
            this.prevPlace = x.ride.places.at(ind - 1)
            this.nextPlace = x.ride.places.at(ind + 1)
            this.name = this.place.name
            this.origName = this.place.name
            this.routes = this.place.routes
            this.ind = ind
            console.log(xplace); 
          }
      }
      
    })
  }

  routeSelected(route: Route) {
    this.store.dispatch(PreviewRouteSelectedAction({route: route}))
  }

  async preview() {
    if(this.place === undefined) return;
    this.place = await this.mapService.createPlaceByName(this.name, this.ind)
    this.store.dispatch(UpdateEditedPlace({place: this.place!}))
    if(this.nextPlace === undefined) return;
    this.routingService.getRoutes([this.place!.point!, this.nextPlace!.point!]).then(x => {
      this.store.dispatch(UpdateRoutes({place: this.nextPlace!, routes: x}))
    })

  }

}

