import { Component, Input, OnInit } from '@angular/core';
import { Place, Ride, Route } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';
import { OSRM, IOsrmWaypoint } from 'osrm-rest-client';
import { decode, encode } from "@googlemaps/polyline-codec";
import { AppState } from 'src/app/store/ride.reducer';
import { Store } from '@ngrx/store';
import { PreviewRouteSelectedAction } from 'src/app/store/ride.actions';


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
  routes: Array<Route> = [];
  osrm = OSRM();
  ind: number = -1

  constructor(private mapService: MapService, private store: Store<{state: AppState}>) { }

  ngOnInit(): void {
    this.store.select('state').subscribe(x => {
      for (const [ind, xplace] of x.ride.places.entries()) {
          if(xplace.editing) {
            this.place = xplace;
            this.prevPlace = x.ride.places.at(ind - 1)
            this.name = this.place.name
            this.origName = this.place.name
            this.routes = this.place.routes
            this.ind = ind
          }
      }
      
    })
  }

  routeSelected(route: Route) {
    this.store.dispatch(PreviewRouteSelectedAction({route: route}))
  }


}
