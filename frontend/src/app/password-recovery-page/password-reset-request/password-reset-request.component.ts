import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiResponse } from 'src/models/responses';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-password-reset-request',
  templateUrl: './password-reset-request.component.html',
  styleUrls: ['./password-reset-request.component.sass']
})
export class PasswordResetRequestComponent implements OnInit {

  requestForm!: FormGroup;

  constructor(
    private builder: FormBuilder,
    private toastr: Toastr,
    private authService: AuthService,
    private router: Router
  )
  { 
    this.createForm();
  }

  private createForm() : void{
    this.requestForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
    }) 
  }

  ngOnInit(): void {
  }

  requestReset() : void {
    if(this.requestForm.valid){
      this.sendPasswordResetRequest();
    }
    else{
      this.toastr.error("Please check again the email you entered");
    }
    
  }

  private sendPasswordResetRequest() : void {
    this.authService.requestPasswordReset(this.requestForm.value).subscribe({
      next: () => {
        this.router.navigate(['/password-recovery/request-success'], {skipLocationChange: true});
      },
      error: (resp: HttpErrorResponse) => {
        this.handleResetRequestError(resp.error)  
      }
    })    
  }

  private handleResetRequestError(error: ApiResponse){
    console.log(error)
    if(error.status === HttpStatusCode.NotFound){
      this.toastr.error(error.message, "Please try again!");
    }
    else if(error.status === HttpStatusCode.UnprocessableEntity){
      this.toastr.error(error.message, "Please try again!");
    }
    else{
      this.toastr.error("Request for password reset failed!", "Please try again!");
    }      
  }

  get email() { return this.requestForm.get('email'); }
}
