import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DriverRegistrationRequest } from 'src/models/driver';
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

  registerDriver(request: DriverRegistrationRequest){
    const url = environment.API_BASE_URL + `/accounts/drivers/`;
    return this.httpRequestService.post(url, request) as Observable<any>;
  }
}
