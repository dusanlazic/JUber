import { Component, OnInit } from '@angular/core';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-login-social',
  templateUrl: './login-social.component.html',
  styleUrls: ['./login-social.component.sass']
})
export class LoginSocialComponent implements OnInit {

  GOOGLE_AUTH_URL= environment.GOOGLE_AUTH_URL_LOGIN;
  FACEBOOK_AUTH_URL= environment.FACEBOOK_AUTH_URL_LOGIN;

  constructor() { }

  ngOnInit(): void {
  }

}
