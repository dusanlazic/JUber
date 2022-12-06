import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { AuthService } from 'src/services/auth/auth.service';
import { LocalStorageService } from 'src/services/util/local-storage.service';
import { ParserUtil } from 'src/services/util/parser-util.service';
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
    const expiresAt = Number(ParserUtil.getUrlParameter('expiresAt', this.router.url));
    const error = ParserUtil.getUrlParameter('error', this.router.url);

    if(expiresAt) {
        this.authService.handleSuccessfulAuth(expiresAt, '/home');  // authenticated
    } else {
        console.log(error);
        this.toastr.error('Oops! Something went wrong. Please try again!');
        this.router.navigate(['/']);
    }

   }

  ngOnInit(): void {
  }

  


}
