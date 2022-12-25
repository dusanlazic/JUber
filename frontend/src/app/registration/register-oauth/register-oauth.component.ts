import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiResponse } from 'src/models/responses';
import { LoggedUser, Roles } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { ValidationConstants } from 'src/services/util/custom-validators';
import { LocalStorageService } from 'src/services/util/local-storage.service';
import { ParserUtil } from 'src/services/util/parser-util.service';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-register-oauth',
  templateUrl: './register-oauth.component.html',
  styleUrls: ['./register-oauth.component.sass']
})
export class RegisterOauthComponent implements OnInit {

  registrationForm!: FormGroup
  loggedUserFirstName!: string;

  constructor(
    private builder: FormBuilder,
    private toastr: Toastr,
    private authService: AuthService,
    private router: Router,
    private localStorage: LocalStorageService
  ){ 
    this.createForm();
  }

  private createForm() : void {
    this.registrationForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      firstName: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      lastName: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      city: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name)]),
      phoneNumber: new FormControl('', [Validators.required, Validators.pattern('^\\+[1-9][0-9]{3,14}$')]) // custom
    })
    this.email?.disable()
  }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (user: LoggedUser) => {
        this.patchLoggedUser(user);
      },
      error: (e: HttpErrorResponse) => {
        console.log(e)
      }
    })
  }

  private patchLoggedUser(user: LoggedUser) : void {
    const nameParts: string[] = ParserUtil.separateName(user.name);
    this.loggedUserFirstName = ParserUtil.capitalizeWord(nameParts[0]);

    this.registrationForm.patchValue({
      firstName: nameParts[0],
      lastName: nameParts[1],
      email: user.email
    })
  }

  logout() : void {
    this.authService.logout()
    this.router.navigate(['/login']);
  }

  register() : void {
    this.authService.oauthSignup(this.registrationForm.value).subscribe({
      next: () => {
        this.localStorage.set('role', Roles.PASSENGER);
        this.router.navigate(['/home']);    // authenticated
      },
      error: (e: HttpErrorResponse) => {
        this.handleErrorRegistration(e);
      }
    })
  }

  private handleErrorRegistration(error: ApiResponse) : void {
    if(error?.status === HttpStatusCode.Conflict){
      this.toastr.error(error.message);
    }
    else if(error?.status === HttpStatusCode.Forbidden){
      this.toastr.error("You might already have an account", "Registration failed!");
    }
    else{
      console.log(error)
      this.toastr.error("Please try again!", "Registration failed!");
    }    
  }

  get firstName() { return this.registrationForm.get('firstName'); }
  get lastName() { return this.registrationForm.get('lastName'); }
  get city() { return this.registrationForm.get('city'); }
  get phoneNumber() { return this.registrationForm.get('phoneNumber'); }
  get email() { return this.registrationForm.get('email'); }
}
