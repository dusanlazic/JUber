import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PastRidesResponse } from 'src/app/shared/profile-page/profile-navigation/past-rides/past-rides.component';
import { environment } from 'src/environments/environment';
import { DriverInfo, DriverRegistrationRequest } from 'src/models/driver';
import { RideReview } from 'src/models/rideReview';
import { HttpRequestService } from '../util/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  constructor(private httpRequestService: HttpRequestService) {}

  activate(email: string): Observable<any> {
      const url = environment.API_BASE_URL + `/accounts/drivers/activate`;

      return this.httpRequestService.patch(url, email) as Observable<any>;
  }

  inactivate(email: string): Observable<any> {
    const url = environment.API_BASE_URL + `/accounts/drivers/inactivate`;

    return this.httpRequestService.patch(url, email) as Observable<any>;
  }

  getStatuses(): Observable<any> {
    const url = environment.API_BASE_URL + `/accounts/drivers/statuses`;
    return this.httpRequestService.get(url) as Observable<any>;
  }

  getDriversInfo(driverId: string): Observable<DriverInfo> {
    const url = environment.API_BASE_URL + `/accounts/drivers/${driverId}/info`;
    return this.httpRequestService.get(url) as Observable<DriverInfo>;
  }

  getDriversPastRides(driverId: string): Observable<Array<PastRidesResponse>> {
    const url = environment.API_BASE_URL + `/accounts/drivers/${driverId}/rides`;
    return this.httpRequestService.get(url) as Observable<Array<PastRidesResponse>>;
  }

  getDriversReviews(driverId: string): Observable<Array<RideReview>> {
    const url = environment.API_BASE_URL + `/accounts/drivers/${driverId}/reviews`;
    return this.httpRequestService.get(url) as Observable<Array<RideReview>>;
  }

  registerDriver(request: DriverRegistrationRequest){
    const url = environment.API_BASE_URL + `/accounts/drivers/`;
    return this.httpRequestService.post(url, request) as Observable<any>;
  }
}
