import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Point, Route } from 'src/models/map';

@Injectable({
  providedIn: 'root'
})
export class RoutingService {

  ROUTING_URL = 'http://router.project-osrm.org/route/v1/driving'

  constructor(private httpClient: HttpClient) { }


  getRoutes(locations: Point[]): Observable<Route> {
    let locs = locations.reduce((a, b) => a + `${b.longitude},${b.latitude};`, '')
    let url: string = `${this.ROUTING_URL}/` 
    return this.httpClient.get('url')/*.pipe(
      map( (data: any) => data.map(datum => new Route()) )
    );*/
  }

}
