import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { RideRequest } from 'src/models/rideRequest';
import { HttpRequestService } from '../util/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class RideService {


  constructor(private httpRequestService: HttpRequestService) {}

  sendRideRequest(rideRequest: RideRequest): Observable<any> {
      const url = environment.API_BASE_URL + "/ride/rideRequest";
      const body = JSON.stringify(rideRequest);
      console.log(rideRequest);
      
      return this.httpRequestService.post(url, body) as Observable<any>;
  }

  acceptRide(rideId: string): Observable<any> {
      const url = environment.API_BASE_URL + `/ride/accept/${rideId}`;
      return this.httpRequestService.put(url, null) as Observable<any>;
  }

  declineRide(rideId: string): Observable<any> {
    const url = environment.API_BASE_URL + `/ride/decline/${rideId}`;
    return this.httpRequestService.put(url, null) as Observable<any>;
  }

  startRide(rideId: string): Observable<any> {
    const url = environment.API_BASE_URL + `/simulation/start-ride/${rideId}`;
    return this.httpRequestService.put(url, null) as Observable<any>;
  }

  abandonRide(rideId: string, reason: string): Observable<any> {
    const url = environment.API_BASE_URL + `/ride/abandon/${rideId}`;
    const body = JSON.stringify({reason: reason});
    return this.httpRequestService.put(url, body) as Observable<any>;
  }

  panic(rideId: string) {
    const url = environment.API_BASE_URL + `/ride/panic/${rideId}`;
    return this.httpRequestService.put(url, {}) as Observable<any>;
  }

  endRide(rideId: string): Observable<any> {
    const url = environment.API_BASE_URL + `/simulation/end-ride/${rideId}`;
    return this.httpRequestService.put(url, {}) as Observable<any>;
  }

  getPastRides(): Observable<any> {
    const url = environment.API_BASE_URL + '/ride/pastRides';
    return this.httpRequestService.get(url) as Observable<any>;
  }

  getSavedRoutes(): Observable<any> {
    const url = environment.API_BASE_URL + "/ride/savedRoutes";
    return this.httpRequestService.get(url) as Observable<any>;
  }
}
