import { Injectable } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth/auth.service';
import { HttpRequestService } from './util/http-request.service';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(private httpRequestService: HttpRequestService) {}
}
