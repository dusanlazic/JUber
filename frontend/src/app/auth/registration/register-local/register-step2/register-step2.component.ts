import { Component, Output, Input, EventEmitter } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { LocalRegistrationInputs, PersonalInfo } from 'src/models/auth';
import { ValidationConstants } from 'src/services/util/custom-validators';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-register-step2',
  templateUrl: './register-step2.component.html',
  styleUrls: ['./register-step2.component.sass']
})
export class RegisterStep2Component {
  @Output() nextStepEvent = new EventEmitter<LocalRegistrationInputs>();
  @Output() previousStepEvent = new EventEmitter<LocalRegistrationInputs>();
  
  registrationForm!: FormGroup;
  
  constructor(
    private builder: FormBuilder,
    private toastr: Toastr
  ) 
  { 
    this.createForm();
  }

  private createForm(): void {
    this.registrationForm = this.builder.group({
      firstName: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      lastName: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      city: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name)]),
      phoneNumber: new FormControl('', [Validators.required, Validators.pattern('^\\+[1-9][0-9]{3,14}$')]) // custom
    });
  }

  previousStep() : void {
    this.previousStepEvent.emit({step2Inputs:  this.registrationForm.value});
  }

  nextStep() : void {
    if(this.registrationForm.valid){
      this.nextStepEvent.emit({step2Inputs:  this.registrationForm.value});
    }  
    else{
      this.toastr.error("Please check your inputs again")
    }
  }

  get firstName() { return this.registrationForm.get('firstName'); }
  get lastName() { return this.registrationForm.get('lastName'); }
  get city() { return this.registrationForm.get('city'); }
  get phoneNumber() { return this.registrationForm.get('phoneNumber'); }

  @Input()
  set step2Inputs(inputs: PersonalInfo) {
    if(inputs){
      this.registrationForm.patchValue(inputs);
    }     
  }
}
