import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiResponse } from 'src/models/responses';
import { AuthService } from 'src/services/auth/auth.service';
import { ParserUtil } from 'src/services/util/parser-util.service';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.sass']
})
export class EmailVerificationComponent implements OnInit {

  success: boolean | undefined
  private verificationToken!: string;

  constructor(
    private router: Router,
    private authService: AuthService,
    private toastr: Toastr
  ) { }

  ngOnInit(): void {
    this.getVerificationTokenFromUrl();
    this.verifyEmail();
  }

  private getVerificationTokenFromUrl() : void {
    const token = ParserUtil.getUrlParameter('token', this.router.url);
    if(token === null){
      this.success = false
    }
    this.verificationToken = token;
  }

  private verifyEmail() : void {
    if(!this.verificationToken){
       this.success = false
       return;
    }
    
    this.authService.verifyEmail(this.verificationToken).subscribe({
      next: () => {
        this.success=true;
      },
      error: (e: HttpErrorResponse) => {
        this.success=false;
        this.handleVerificationError(e.error);
      }
    })
  }

  private handleVerificationError(error: ApiResponse){
    console.log(error)
    if(error.status === HttpStatusCode.NotFound){
      this.toastr.error(error.message, "Please try again!");
    }
    else if(error.status === HttpStatusCode.Unauthorized){
      this.toastr.error(error.message, "Please try again!");
    }
    else{
      this.toastr.error("Request for password reset failed!", "Please try again!");
    }      
  }

}
