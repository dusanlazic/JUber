import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DriverService } from 'src/services/driver/driver.service';
import { CustomValidators, ValidationConstants } from 'src/services/util/custom-validators';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-driver-registration',
  templateUrl: './driver-registration.component.html',
  styleUrls: ['./driver-registration.component.sass']
})
export class DriverRegistrationComponent implements OnInit {

  registrationForm!: FormGroup;
  
  constructor(
    private builder: FormBuilder,
    private toastr: Toastr,
    private driverService: DriverService,
    private router: Router
  ) 
  { 
    this.createForm();
  }

  ngOnInit(): void {
    
  }

  private createForm(): void {
    this.registrationForm = this.builder.group({
      firstName: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      lastName: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      city: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name)]),
      phoneNumber: new FormControl('', [Validators.required, Validators.pattern('^\\+[1-9][0-9]{3,14}$')]),
      
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      passwordConfirmation: new FormControl('', [Validators.required]),

      capacity: new FormControl(1),
      babyFriendly: new FormControl(false),
      petFriendly: new FormControl(false),
    },{
      validators: [CustomValidators.MatchValidator('password', 'passwordConfirmation')]
    });
  }

  createAccount(): void {
    console.log(this.registrationForm.value)
    this.driverService.registerDriver(this.registrationForm.value).subscribe({
      next: (res: any) => {
        console.log(res);
        this.toastr.success(`Successfully registered new driver ${this.firstName?.value} ${this.lastName?.value}`)
        this.router.navigate(['/admin/drivers']);
      },
      error: (err: any) => {
        this.toastr.error(err.error.message)
      }
    })
  }

  get firstName() { return this.registrationForm.get('firstName'); }
  get lastName() { return this.registrationForm.get('lastName'); }
  get city() { return this.registrationForm.get('city'); }
  get phoneNumber() { return this.registrationForm.get('phoneNumber'); }

  get email() { return this.registrationForm.get('email'); }
  get password() { return this.registrationForm.get('password'); }
  get passwordConfirmation() { return this.registrationForm.get('passwordConfirmation'); }
}
