import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from "@auth0/angular-jwt";
import { BehaviorSubject, Observable, ReplaySubject, Subject } from "rxjs";
import { environment } from "src/environments/environment";
import { LocalRegistrationRequest, LoginRequest, PasswordReset, PasswordResetLinkRequest, PersonalInfo, TokenResponse } from "src/models/auth";
import { ApiResponse } from 'src/models/responses';
import { LoggedUser, Roles } from 'src/models/user';
import { DriverService } from '../driver/driver.service';
import { HttpRequestService } from "../util/http-request.service";
import { LocalStorageService } from "../util/local-storage.service";
import { CookieService } from 'ngx-cookie-service';
import { NotificationWebSocketAPI } from '../notification/notification-socket.service';
import { RideWebSocketAPI } from '../ride/ride-message.service';
const jwtHelper = new JwtHelperService();



@Injectable({
    providedIn: 'root'
})

export class AuthService {

    private loggedUser!: LoggedUser | undefined;
    private loggedUserSubject = new BehaviorSubject<LoggedUser | undefined>(undefined);

    getNewLoggedUser(): Observable<LoggedUser | undefined> {
        return this.loggedUserSubject.asObservable();
    }

    onNewUserReceived(msg: LoggedUser | undefined) {        
        this.loggedUserSubject.next(msg);
    }

    constructor(
        private httpRequestService: HttpRequestService,
        private localStorage: LocalStorageService,
        private router: Router,
        private driverService: DriverService,
        private cookieService: CookieService,
    ) {
        this.getCurrentUser().subscribe({
            next: (user: LoggedUser) => {
                this.loggedUser = user;
                this.onNewUserReceived(user);
            }
        });
    }


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
        const tokenExpiration = this.localStorage.getTokenExpiration();
        if(tokenExpiration) {
            return Date.now() < tokenExpiration;
        }
        return false;
    }

    handleSuccessfulAuth(expiresAt: number, redirectPath?: string) : void {
        this.localStorage.setTokenExpiration(expiresAt);

        this.getCurrentUser().subscribe({
            next: (user: LoggedUser) => {
                this.localStorage.set('role', user.role);
                if(user.role === Roles.ADMIN){
                    redirectPath = '/admin'
                }
                if(user.role === Roles.PASSENGER_NEW){
                    redirectPath = '/registration/social'
                }
                this.loggedUser = user;
                this.onNewUserReceived(user);
                if(redirectPath)
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
        if(this.loggedUser?.role === Roles.DRIVER){
            this.driverService.inactivate(this.loggedUser.email);
        }

        const url = environment.API_BASE_URL + "/auth/logout";
        this.httpRequestService.post(url, null)
        this.loggedUser = undefined;
        this.localStorage.clearAll();
        sessionStorage.clear();
        localStorage.clear();
        this.cookieService.deleteAll();
        this.onNewUserReceived(this.loggedUser);
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

    imageUrlResolver(event: any, name: string) {
        event.target.src = "https://ui-avatars.com/api/?name=" + name + "&format=png&background=random&rounded=true&size=128x128";
    }
}


