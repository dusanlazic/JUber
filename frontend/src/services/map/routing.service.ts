import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Point } from 'src/models/map';
import { Route } from 'src/models/ride';
import { OSRM, IOsrmWaypoint } from 'osrm-rest-client';
import { some } from 'lodash';
import { decode, encode } from "@googlemaps/polyline-codec";


@Injectable({
  providedIn: 'root'
})
export class RoutingService {

  ROUTING_URL = 'http://router.project-osrm.org/route/v1/driving'

  osrm = OSRM();

  constructor(private httpClient: HttpClient) {

  }

  async getRoutes(locations: Point[]): Promise<Route[]> {
    let routes = [];
    const apiRoutes = (await this.osrm.route({
      coordinates: locations.map(point => [point.longitude, point.latitude]),
      steps: true,
      alternatives: true,
      overview: 'simplified',
      geometries: 'polyline',
      annotations: true
    })).routes;

    let selected = false;
    for(let apiRoute of apiRoutes) {
      let route = new Route();
      route.selected = selected ? false : true;
      selected = true;
      route.coordinates = decode(apiRoute.geometry!).map(x => new Point(x[0], x[1]))
      route.distance = apiRoute.distance!.valueOf()
      route.name = apiRoute.legs![0].summary!
      routes.push(route)
    }

    return routes;
    
  }

}
