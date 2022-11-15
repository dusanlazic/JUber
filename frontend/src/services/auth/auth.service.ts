import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from "@auth0/angular-jwt";
import { BehaviorSubject, Observable, ReplaySubject, Subject } from "rxjs";
import { environment } from "src/environments/environment";
import { LocalRegistrationRequest, LoginRequest, TokenResponse } from "src/models/auth";
import { LoggedUser } from 'src/models/user';
import { HttpRequestService } from "../util/http-request.service";
import { LocalStorageService } from "../util/local-storage.service";

const jwtHelper = new JwtHelperService();

@Injectable({
    providedIn: 'root'
})

export class AuthService {

    private loggedUser!: LoggedUser;

    constructor(
        private httpRequestService: HttpRequestService,
        private localStorage: LocalStorageService,
        private router: Router
    ) {}


    login(loginRequest: LoginRequest) : Observable<TokenResponse> {
        const url = environment.API_BASE_URL + "/auth/login";
        const body = JSON.stringify(loginRequest);
        
        return this.httpRequestService.post(url, body) as Observable<TokenResponse>;
    }


    signup(signupRequest: LocalRegistrationRequest) : Observable<any> {
        const url = environment.API_BASE_URL + "/auth/register";
        const body = JSON.stringify(signupRequest);

        return this.httpRequestService.post(url, body) as Observable<any>;
    }

    isAuthenticated(): boolean {
        const token = this.localStorage.getToken();
        if(token) {
            return !jwtHelper.isTokenExpired(token);
        }
        return false;
    }

    handleSuccessfulLogin(token: string) : void {
        this.localStorage.setToken(token);

        this.getCurrentUser().subscribe({
            next: (user: LoggedUser) => {
                this.localStorage.set('role', user.role);
                this.loggedUser = user;
                this.router.navigate(['/profile']);
            },
            error: (e: HttpErrorResponse) => {
                console.log(e);
                // this.router.navigate(['/']);
            }
        })
    }

    getCurrentUser() : Observable<LoggedUser>{
        if(this.loggedUser){
            return new Observable(observer => {
                observer.next(this.loggedUser); 
            });
        }
        
        const url = environment.API_BASE_URL + "/auth/me";
        return this.httpRequestService.get(url) as Observable<LoggedUser>;
    }

    logout() : void {
        this.localStorage.clearAll();
    }
}