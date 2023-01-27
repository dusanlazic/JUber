import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PastRidesResponse } from 'src/app/profile/profile-navigation/past-rides/past-rides.component';
import { environment } from 'src/environments/environment';
import { PersonDTO } from 'src/models/user';
import { HttpRequestService } from '../util/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class PassengerService {

  constructor(private httpRequestService: HttpRequestService) { }

  getPassengerInfo(passengerId: string): Observable<PersonDTO> {
    const url = environment.API_BASE_URL + `/passengers/${passengerId}/info`;
    return this.httpRequestService.get(url) as Observable<PersonDTO>;
  }

  getPassengerPastRides(passengerId: string): Observable<Array<PastRidesResponse>> {
    const url = environment.API_BASE_URL + `/passengers/${passengerId}/rides`;
    return this.httpRequestService.get(url) as Observable<Array<PastRidesResponse>>;
  }

  findAll(): Observable<Array<PersonDTO>> {
    const url = environment.API_BASE_URL + `/passengers/findAll`;
    return this.httpRequestService.get(url) as Observable<Array<PersonDTO>>;
  }
}
