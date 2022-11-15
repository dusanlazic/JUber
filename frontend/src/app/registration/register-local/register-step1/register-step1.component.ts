import { Component, OnInit, Output, EventEmitter  } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Toastr } from 'src/services/util/toastr.service';

export class CustomValidators {
  static MatchValidator(source: string, target: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const sourceCtrl = control.get(source);
      const targetCtrl = control.get(target);

      return sourceCtrl && targetCtrl && sourceCtrl.value !== targetCtrl.value
        ? { mismatch: true }
        : null;
    };
  }
}

type RegistrationStep1 = {
  email: string,
  password: string,
  confirmedPassword: string
}

@Component({
  selector: 'app-register-step1',
  templateUrl: './register-step1.component.html',
  styleUrls: ['./register-step1.component.sass']
})
export class RegisterStep1Component implements OnInit {

  @Output() nextStepEvent = new EventEmitter<RegistrationStep1>();
  
  registrationForm!: FormGroup;

  constructor(
    private builder: FormBuilder,
    private toastr: Toastr
  ){ }

  ngOnInit(): void {
    this.registrationForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
      confirmPassword: new FormControl('', [Validators.required])
    }, [CustomValidators.MatchValidator('password', 'confirmPassword')]);
  }


  nextStep() : void {
    /*if(this.registrationForm.valid){
      this.nextStepEvent.emit(this.registrationForm.value);
    }  
    this.toastr.error("Please check your inputs again")  */
    this.nextStepEvent.emit(this.registrationForm.value);
  }

  arePasswordsSame() : boolean {
    console.log(this.confirmPassword)
    console.log(this.password)
    return this.confirmPassword === this.password
  }


  get email() { return this.registrationForm.get('email'); }
  get password() { return this.registrationForm.get('password'); }
  get confirmPassword() { return this.registrationForm.get('confirmPassword'); }
  get passwordMatchError() {
    return (
      this.registrationForm.getError('mismatch') &&
      this.registrationForm.get('confirmPassword')?.touched
    );
  }
}
