import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-oauth2-redirect-handler',
  templateUrl: './oauth2-redirect-handler.component.html',
  styleUrls: ['./oauth2-redirect-handler.component.sass']
})
export class Oauth2RegisterRedirectHandlerComponent implements OnInit {

  constructor(
    public router: Router,
    private authService: AuthService,
    private toastr: Toastr,
  ) {}

  ngOnInit(): void {
    const token = this.getUrlParameter('token');
    const error = this.getUrlParameter('error');

    if(token) {
        this.authService.handleSuccessfulAuth(token, '/registration/social');
    } else {
        console.log(error);
        this.toastr.error('Oops! Something went wrong. Please try again!');
        this.router.navigate(['/']);
    }
  }

  getUrlParameter(name: string) : string{
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    const [_, search] = this.router.url.split('?');
    var results = regex.exec("?".concat(search));
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  };
}
