import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

type RegistrationStep2 = {
  firstName: string,
  lastName: string,
  city: string,
  phoneNumber: string,
}

@Component({
  selector: 'app-register-step2',
  templateUrl: './register-step2.component.html',
  styleUrls: ['./register-step2.component.sass']
})
export class RegisterStep2Component implements OnInit {
  @Output() nextStepEvent = new EventEmitter<RegistrationStep2>();
  @Output() previousStepEvent = new EventEmitter<void>();
  
  registrationForm!: FormGroup;

  constructor(private builder: FormBuilder) { }

  ngOnInit(): void {
    this.registrationForm = this.builder.group({
      firstName: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
      lastName: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
      city: new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
      phoneNumber: new FormControl('', [Validators.required, Validators.pattern('\\+[0-9]*')]) // custonm
    });
  }

  previousStep() : void {
    this.previousStepEvent.emit(this.registrationForm?.value);
  }

  nextStep() : void {
    /*if(this.registrationForm.valid){
      this.nextStepEvent.emit(this.registrationForm.value);
    }  
    this.toastr.error("Please check your inputs again")  */
    this.nextStepEvent.emit(this.registrationForm?.value);
  }

  
  get firstName() { return this.registrationForm.get('firstName'); }
  get lastName() { return this.registrationForm.get('lastName'); }
  get city() { return this.registrationForm.get('city'); }
  get phoneNumber() { return this.registrationForm.get('phoneNumber'); }
}
