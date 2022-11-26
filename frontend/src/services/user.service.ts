import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpRequestService } from './util/http-request.service';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(private httpRequestService: HttpRequestService) {}

    getPassengerBasicInfo(email: string): Observable<any> {
        const url = environment.API_BASE_URL + `/passengers/basicInfo/${email}`;

        return this.httpRequestService.get(url) as Observable<any>;
    }
}
