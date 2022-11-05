import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { LocalStorageService } from 'src/services/localStorage.service';
import { LoginService } from 'src/services/login.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;

  constructor(
    private builder: FormBuilder, 
    private loginService: LoginService,
    private router: Router,
    private localStorageService: LocalStorageService
  ){ }

  ngOnInit(): void {
    this.loginForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required])
    });
  }


  login() {
    console.log(this.loginForm.value);
    this.loginService.login(this.loginForm.value)
      .subscribe({
        next: (response) => {
          console.log(response); 
          console.log("You're successfully logged in!"); 
          this.localStorageService.set(environment.ACCESS_TOKEN, response.accessToken);
          this.router.navigate(['/profile']);
        },
        error: (e) => {console.error(e); console.log('Oops! Something went wrong. Please try again!')},
        complete: () => console.info('complete') 
    });
  }

  
}

