import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PasswordReset } from 'src/models/auth';
import { ApiResponse } from 'src/models/responses';
import { UserService } from 'src/services/user.service';
import { CustomValidators } from 'src/services/util/custom-validators';
import { ParserUtil } from 'src/services/util/parser-util.service';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-password-reset-form',
  templateUrl: './password-reset-form.component.html',
  styleUrls: ['./password-reset-form.component.sass']
})
export class PasswordResetFormComponent implements OnInit {

  resetPasswordForm!: FormGroup;
  private resetToken!: string;
  isRedirectSuccess: boolean | undefined;

  constructor(
    private builder: FormBuilder,
    private toastr: Toastr,
    private userService: UserService,
    private router: Router
  )
  { 
    this.createForm();
  }

  private createForm() : void{
    this.resetPasswordForm = this.builder.group({
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      passwordConfirmation: new FormControl('', [Validators.required])
    },  
    {
      validators: [CustomValidators.MatchValidator('password', 'passwordConfirmation')]
    });
  }

  ngOnInit(): void {
    this.getResetPasswordTokenFromUrl();
  }

  private getResetPasswordTokenFromUrl() : void {
    const token = ParserUtil.getUrlParameter('token', this.router.url);
    if(token === null){
      this.isRedirectSuccess = false;
    }
    else{
      this.isRedirectSuccess = true;
      this.resetToken = token;
    }
    
  }


  resetPassword() : void {
    if(this.resetPasswordForm.valid){
      this.sendResetPassword();
    }
    else{
      this.toastr.error("Please check again the email you entered");
    }
    
  }

  private sendResetPassword() : void {
    const passwordReset : PasswordReset = {...this.resetPasswordForm.value, token: this.resetToken}

    this.userService.resetPassword(passwordReset).subscribe({
      next: () => {
        this.router.navigate(['/password-recovery/reset-success'], {skipLocationChange: true});
      },
      error: (resp: HttpErrorResponse) => {
        this.handleResetError(resp.error)  
      }
    })    
  }

  private handleResetError(error: ApiResponse){
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

  get password() { return this.resetPasswordForm.get('password'); }
  get passwordConfirmation() { return this.resetPasswordForm.get('passwordConfirmation'); }

}
