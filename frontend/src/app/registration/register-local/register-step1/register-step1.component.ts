import { Component, Output, Input, EventEmitter  } from '@angular/core';
import {  FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { LocalRegistrationInputs, AccountInfo } from 'src/models/auth';
import { CustomValidators } from 'src/services/util/custom-validators';
import { Toastr } from 'src/services/util/toastr.service';


@Component({
  selector: 'app-register-step1',
  templateUrl: './register-step1.component.html',
  styleUrls: ['./register-step1.component.sass']
})
export class RegisterStep1Component {

  @Output() nextStepEvent = new EventEmitter<LocalRegistrationInputs>();
  
  registrationForm!: FormGroup

  constructor(
    private builder: FormBuilder,
    private toastr: Toastr
  )
  { 
    this.createForm();
  }

  private createForm() : void{
    this.registrationForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      passwordConfirmation: new FormControl('', [Validators.required])
    },  
    {
      validators: [CustomValidators.MatchValidator('password', 'passwordConfirmation')]
    });
  }

  nextStep() : void {
    if(this.registrationForm.valid){
      this.nextStepEvent.emit({step1Inputs:  this.registrationForm.value});
    }
    else{
      this.toastr.error("Please check your inputs again!")
    }
  }

  get email() { return this.registrationForm.get('email'); }
  get password() { return this.registrationForm.get('password'); }
  get passwordConfirmation() { return this.registrationForm.get('passwordConfirmation'); }

  @Input()
  set step1Inputs(inputs: AccountInfo) {
    if(inputs){
      this.registrationForm.patchValue(inputs);
    }    
  }
}
