import { Injectable } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PasswordReset, PasswordResetLinkRequest } from 'src/models/auth';
import { HttpRequestService } from './util/http-request.service';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(private httpRequestService: HttpRequestService) {}

    requestPasswordReset(resetRequest: PasswordResetLinkRequest): Observable<any> {
        const url = environment.API_BASE_URL + "/auth/recovery";
        const body = JSON.stringify(resetRequest);

        return this.httpRequestService.post(url, body) as Observable<any>;
    }

    resetPassword(passwordReset: PasswordReset): Observable<any> {
        const url = environment.API_BASE_URL + "/auth/recovery";
        const body = JSON.stringify(passwordReset);

        return this.httpRequestService.patch(url, body) as Observable<any>;
    }
}
