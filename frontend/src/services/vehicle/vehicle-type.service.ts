import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IVehicleType } from 'src/app/store/rideRequest/rideRequest';
import { environment } from 'src/environments/environment';
import { HttpRequestService } from '../util/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class VehicleTypeService {

  constructor(private httpRequestService: HttpRequestService) {}

    findAll(): Observable<IVehicleType[]> {
        const url = environment.API_BASE_URL + "/vehicleType/findAll";

        return this.httpRequestService.get(url) as Observable<IVehicleType[]>;
    }
}
