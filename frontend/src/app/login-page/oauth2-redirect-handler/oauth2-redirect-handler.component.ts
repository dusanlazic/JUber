import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { AuthService } from 'src/services/auth/auth.service';
import { LocalStorageService } from 'src/services/util/local-storage.service';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-oauth2-redirect-handler',
  templateUrl: './oauth2-redirect-handler.component.html',
  styleUrls: ['./oauth2-redirect-handler.component.sass']
})
export class Oauth2RedirectHandlerComponent implements OnInit {

  constructor(
    public router: Router,
    private authService: AuthService,
    private toastr: Toastr
  ) {

    const token = this.getUrlParameter('token');
    const error = this.getUrlParameter('error');

    if(token) {
        this.authService.handleSuccessfulLogin(token);
    } else {
        console.log(error);
        this.toastr.error('Oops! Something went wrong. Please try again!');
        this.router.navigate(['/']);
    }

   }

  ngOnInit(): void {
  }

  getUrlParameter(name: string) : string{
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    const [_, search] = this.router.url.split('?');
    var results = regex.exec("?".concat(search));
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  };


}
