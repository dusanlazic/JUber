import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { LoginRequest } from "src/models/auth";

@Injectable({
    providedIn: 'root'
})

export class AuthService {

    constructor(private httpClient: HttpClient) {}

    createHeaders(): HttpHeaders {
        return new HttpHeaders({
            'Content-type': 'application/json'
        });
    }

    login(loginRequest: LoginRequest) : Observable<any> {
        const url = environment.API_BASE_URL + "/auth/login";
        const body = JSON.stringify(loginRequest);
        const headers = this.createHeaders();
        
        return this.httpClient.post(url, body, {headers}) as Observable<any>;
    }


    signup(signupRequest: any) : Observable<any> {
        const url = environment.API_BASE_URL + "/auth/signup";
        const body = JSON.stringify(signupRequest);
        const headers = this.createHeaders();

        return this.httpClient.post(url, body, {headers}) as Observable<any>;
    }

}