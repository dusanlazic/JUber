import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { environment } from 'src/environments/environment';
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
    private loginService: LoginService
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
        .subscribe(response => {
            if(response){
              localStorage.setItem(environment.ACCESS_TOKEN, response.accessToken);
              console.log("You're successfully logged in!");
              // redirect
            }
            else{
              console.log('Oops! Something went wrong. Please try again!');
            }
        });
  }

  
}

