import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from "@auth0/angular-jwt";
import { BehaviorSubject, Observable, ReplaySubject, Subject } from "rxjs";
import { environment } from "src/environments/environment";
import { LocalRegistrationRequest, LoginRequest, PasswordReset, PasswordResetLinkRequest, PersonalInfo, TokenResponse } from "src/models/auth";
import { ApiResponse } from 'src/models/responses';
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


    signup(signupRequest: LocalRegistrationRequest) : Observable<ApiResponse> {
        const url = environment.API_BASE_URL + "/auth/register";
        const body = JSON.stringify(signupRequest);

        return this.httpRequestService.post(url, body) as Observable<ApiResponse>;
    }

    oauthSignup(signupRequest: PersonalInfo) : Observable<ApiResponse> {
        const url = environment.API_BASE_URL + "/auth/register/oauth";
        const body = JSON.stringify(signupRequest);

        return this.httpRequestService.patch(url, body) as Observable<ApiResponse>;
    }

    isAuthenticated(): boolean {
        const token = this.localStorage.getToken();
        if(token) {
            return !jwtHelper.isTokenExpired(token);
        }
        return false;
    }

    handleSuccessfulAuth(token: string, redirectPath: string) : void {
        this.localStorage.setToken(token);

        this.getCurrentUser().subscribe({
            next: (user: LoggedUser) => {
                this.localStorage.set('role', user.role);
                this.loggedUser = user;
                this.router.navigate([redirectPath]);
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


    verifyEmail(token: string) : Observable<any> {
        const url = environment.API_BASE_URL + `/auth/register/verify/${token}`;
        
        return this.httpRequestService.post(url, null) as Observable<any>;
    }

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