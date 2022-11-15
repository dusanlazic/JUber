import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
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
    private authService: AuthService
  ){ 
    this.createForm();
  }

  private createForm() : void {
    this.registrationForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      firstName: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*'), Validators.maxLength(40)]),
      lastName: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*'), Validators.maxLength(40)]),
      city: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
      phoneNumber: new FormControl('', [Validators.required, Validators.pattern('^\\+[1-9][0-9]{3,14}$')]) // custom
    })
    this.email?.disable()
  }

  ngOnInit(): void {
    this.patchLoggedUser();
  }

  patchLoggedUser() : void {
    this.authService.getCurrentUser().subscribe({
      next: (user: LoggedUser) => {
        
        const nameParts: string[] = ParserUtil.separateName(user.name);
        this.loggedUserFirstName = ParserUtil.capitalizeWord(nameParts[0]);

        this.registrationForm.patchValue({
          firstName: nameParts[0],
          lastName: nameParts[1],
          email: user.email
        });
      }
    })
  }

  get firstName() { return this.registrationForm.get('firstName'); }
  get lastName() { return this.registrationForm.get('lastName'); }
  get city() { return this.registrationForm.get('city'); }
  get phoneNumber() { return this.registrationForm.get('phoneNumber'); }
  get email() { return this.registrationForm.get('email'); }
}
