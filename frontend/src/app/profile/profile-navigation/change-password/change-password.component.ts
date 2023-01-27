import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ApiResponse } from 'src/models/responses';
import { AccountService } from 'src/services/account/account.service';
import { CustomValidators } from 'src/services/util/custom-validators';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.sass']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm!: FormGroup;

  constructor(
    private accountService: AccountService,
    private toastr: Toastr,
    private builder: FormBuilder
  ){
    this.createForm();
  }

  ngOnInit(): void {
    
  }

  private createForm() : void{
    this.changePasswordForm = this.builder.group({
      currentPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
      newPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
      passwordConfirmation: new FormControl('', [Validators.required])
    },  
    {
      validators: [CustomValidators.MatchValidator('newPassword', 'passwordConfirmation')]
    });
  }

  changePassword(): void {
    this.accountService.changePassword(this.changePasswordForm.value).subscribe({
      next: () => {
        this.toastr.success('Successfully changed your password', 'Success');
      },
      error: (resp: HttpErrorResponse) => {
        this.handleResetError(resp.error)  
      }
    })
  }

  private handleResetError(error: ApiResponse){
    console.log(error)
    if(error.status === HttpStatusCode.UnprocessableEntity){
      this.toastr.error(error.message, "Please try again!");
    }
    else if(error.status === HttpStatusCode.BadRequest){
      this.toastr.error("Passwords do not match.", "Please try again!");
    }
    else{
      this.toastr.error("Password change failed!", "Please try again!");
    }      
  }

  get password() { return this.changePasswordForm.get('password'); }
  get newPassword() { return this.changePasswordForm.get('newPassword'); }
  get passwordConfirmation() { return this.changePasswordForm.get('passwordConfirmation'); }

}
