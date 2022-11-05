import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { LocalStorageService } from 'src/services/localStorage.service';

@Component({
  selector: 'app-oauth2-redirect-handler',
  templateUrl: './oauth2-redirect-handler.component.html',
  styleUrls: ['./oauth2-redirect-handler.component.sass']
})
export class Oauth2RedirectHandlerComponent implements OnInit {

  constructor(
    public router: Router,
    private localStorageService: LocalStorageService 
  ) {

    const token = this.getUrlParameter('token');
    const error = this.getUrlParameter('error');

    if(token) {
        this.localStorageService.set(environment.ACCESS_TOKEN, token);
        console.log("Got token"); 
        this.router.navigate(['/profile']);
    } else {
        console.log('Oops! Something went wrong. Please try again!')
        this.router.navigate(['/']);
    }

   }

  ngOnInit(): void {
  }

  getUrlParameter(name: string) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    console.log("URL that should preferably contain token or error ");
    console.log(this.router.url);
    const [_, search] = this.router.url.split('?');
    var results = regex.exec("?".concat(search));
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  };


}
