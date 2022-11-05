import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { LoginRequest, LoginResponse } from "src/models/auth";
import { LocalStorageService } from "./localStorage.service";

@Injectable({
    providedIn: 'root'
})

export class LoginService {

    constructor(
        private httpClient: HttpClient, 
        private localStorageService: LocalStorageService
    ) {}
    
    createHeaders(): HttpHeaders {
        const headers = new HttpHeaders({
            'Content-type': 'application/json'
        });
        const storedToken = this.localStorageService.get(environment.ACCESS_TOKEN);

        if(storedToken){
            headers.append('Authorization', 'Bearer ' + storedToken);
        }
        return headers;

    }

    getCurrentUser() : Observable<any>{
        const url = environment.API_BASE_URL + "/auth/me";
        const headers = this.createHeaders();

        return this.httpClient.get(url, {headers}) as Observable<any>;
    }
   
    login(loginRequest: LoginRequest) : Observable<LoginResponse> {
        const url = environment.API_BASE_URL + "/auth/login";
        const body = JSON.stringify(loginRequest);

        return this.httpClient.post(url, body) as Observable<LoginResponse>;
    }


    signup(signupRequest: any) : Observable<LoginResponse> {
        const url = environment.API_BASE_URL + "/auth/signup";
        const body = JSON.stringify(signupRequest);

        return this.httpClient.post(url, body) as Observable<LoginResponse>;
    }

}