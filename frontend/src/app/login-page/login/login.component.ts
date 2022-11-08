import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { TokenResponse } from 'src/models/auth';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';
import { PasswordResetModalComponent } from '../password-reset-modal/password-reset-modal.component';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  @ViewChild('resetPasswordModal')
  resetPasswordModal!: PasswordResetModalComponent;

  loginForm!: FormGroup;

  constructor(
    private builder: FormBuilder, 
    private authService: AuthService,
    private toastr: Toastr
  ){ }

  ngOnInit(): void {
    this.loginForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required])
    });
  }


  login() : void{
    this.authService.login(this.loginForm.value)
      .subscribe({
        next: (response: TokenResponse) => {
          this.authService.handleSuccessfulLogin(response.accessToken);
        },
        error: (e: HttpErrorResponse) => {
          // Invalid credentials
          if(e.status === 401){
            this.toastr.error('Make sure you have activated account.', 'Incorect username or password.');
          }
          // User not found 
          else if (e.status === 404){
            this.toastr.error('Make sure you have activated account.', 'Incorect username or password.');
          }
          console.log(e.error.message)         
        }
    });
  }


  requestPasswordReset() : any {
      this.resetPasswordModal.openModal();
  }

  
}

