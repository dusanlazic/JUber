import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { LocalStorageService } from 'src/services/util/localStorage.service';
import { AuthService } from 'src/services/auth.service';
import { Toastr } from 'src/services/util/toastr.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;

  constructor(
    private builder: FormBuilder, 
    private loginService: AuthService,
    private router: Router,
    private localStorageService: LocalStorageService,
    private toastr: Toastr
  ){ }

  ngOnInit(): void {
    this.loginForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required])
    });
  }


  login() {
    this.loginService.login(this.loginForm.value)
      .subscribe({
        next: (response) => {
          this.localStorageService.set(environment.ACCESS_TOKEN, response.accessToken);
          this.router.navigate(['/profile']);
        },
        error: (e) => {
          this.toastr.error('Oops! Something went wrong. Please try again!');
        }
    });
  }

  
}

