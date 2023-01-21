import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { environment } from 'src/environments/environment';
import { TokenResponse } from 'src/models/auth';
import { ApiResponse } from 'src/models/responses';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.sass']
})
export class LoginPageComponent implements OnInit {

  GOOGLE_AUTH_URL= environment.GOOGLE_AUTH_URL_LOGIN;
  FACEBOOK_AUTH_URL= environment.FACEBOOK_AUTH_URL_LOGIN;

  loginForm!: FormGroup

  constructor(
    private builder: FormBuilder,
    private toastr: Toastr,
    private authService: AuthService,
    private cookieService: CookieService
  )
  { 
    this.createForm();
  }

  private createForm() : void{
    this.loginForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    });
  }

  ngOnInit(): void {
    
  }

  login() : void{
    this.authService.login(this.loginForm.value).subscribe({
        next: (response: TokenResponse) => {
          this.authService.handleSuccessfulAuth(response.expiresAt, '/home'); // authenticated
        },
        error: (e: HttpErrorResponse) => {
          this.handleLoginError(e.error)
        }
    });
  }

  private handleLoginError(e: ApiResponse) : void {
    // Invalid credentials
    if(e.status === HttpStatusCode.Unauthorized){
      this.toastr.error('Make sure you have an activated account.', e.message);
    }
    // User not found 
    else if (e.status === HttpStatusCode.NotFound){
      this.toastr.error('Make sure you have an activated account. ', e.message);
    }
    else{
      this.toastr.error('Oops something went wrong ', e.message);
      console.log(e.message)
    }
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

}
