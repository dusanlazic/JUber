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
}
