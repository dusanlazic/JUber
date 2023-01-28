import { HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { overrides } from 'chart.js/dist/core/core.defaults';
import { Observable } from 'rxjs';
import { LoginRequest, TokenResponse } from 'src/models/auth';
import { ApiResponse } from 'src/models/responses';
import { AuthService } from 'src/services/auth/auth.service';

@Injectable()
export class AuthServiceMock extends AuthService {
  
    override login(loginRequest: LoginRequest): Observable<TokenResponse> {
        if(mockValidLoginRequest===loginRequest){
            return new Observable(observer => {
                observer.next(mockTokenResponse);
            });
        }
        else if(mockInvalidUserEmailLoginRequest===loginRequest){
            return new Observable(observer => {
                observer.error(mockInvalidEmailErrorResponse);
            });
        }
        else if(mockInvalidPasswordLoginRequest===loginRequest){
            return new Observable(observer => {
                observer.error(mockInvalidPasswordErrorResponse);
            });
        }
        return new Observable(observer => {
            observer.error();
        });
        
    }
    override handleSuccessfulAuth(expiresAt: number) : void {
        // this.localStorage.setTokenExpiration(expiresAt);

        // this.getCurrentUser().subscribe({
        //     next: (user: LoggedUser) => {
        //         this.localStorage.set('role', user.role);
        //         const redirectPath = this.roleBasedRedirectPath(user.role);
        //         this.loggedUser = user;
        //         this.onNewUserReceived(user);
        //         this.router.navigate([redirectPath]);
        //     },
        //     error: (e: HttpErrorResponse) => {
        //         console.log(e);
        //         // this.router.navigate(['/']);
        //     }
        // })
    }

}

const mockTokenResponse: TokenResponse =  {
    accessToken: 'accessToken',
    expiresAt: 100000000
};

const mockValidLoginRequest: LoginRequest = {
    email: 'mile.miletic@gmail.com',
    password: 'cascaded'
}

const mockInvalidPasswordLoginRequest: LoginRequest = {
    email: 'petar.petrovic@gmail.com',
    password: 'cascaded1'
}

const mockInvalidUserEmailLoginRequest: LoginRequest = {
    email: 'petar@gmail.com',
    password: 'cascaded1'
}

const mockInvalidEmailFormatLoginRequest: LoginRequest = {
    email: 'petar',
    password: 'cascaded1'
}



const mockInvalidPasswordErrorResponse = { status: HttpStatusCode.Unauthorized, statusText: 'Unauthorized', 
        error: {status: HttpStatusCode.Unauthorized, message: "Invalid password"} };


const mockInvalidEmailErrorResponse = { status: HttpStatusCode.NotFound, statusText: 'Not Found', 
        error: {status: HttpStatusCode.Unauthorized, message: "User not found"} };

export { mockTokenResponse, mockValidLoginRequest, 
        mockInvalidPasswordLoginRequest, mockInvalidUserEmailLoginRequest, mockInvalidEmailFormatLoginRequest, 
        mockInvalidPasswordErrorResponse, mockInvalidEmailErrorResponse };