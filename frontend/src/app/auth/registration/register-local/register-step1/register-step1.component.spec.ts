import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { RegisterStep1Component } from './register-step1.component';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { Toastr } from 'src/services/util/toastr.service';

fdescribe('RegisterStep1Component', () => {
  let component: RegisterStep1Component;
  let fixture: ComponentFixture<RegisterStep1Component>;
  let toastr: Toastr;

  
  beforeEach(async () => {
    TestBed.configureTestingModule({
      imports: [ ReactiveFormsModule, ToastrModule.forRoot() ],
      declarations: [ RegisterStep1Component ],
      providers: [FormBuilder, Toastr],
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterStep1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();

    toastr = TestBed.get(Toastr);

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have invalid form on init', () => {
    expect(component.registrationForm.valid).toBeFalsy();
  });

  it('email control should be invalid when empty', () => {
    let email = component.email!;
    email.setValue('');
    expect(email.valid).toBeFalsy();
  });

  it('password control should be invalid when empty', () => {
    let password = component.password!;
    password.setValue('');
    expect(password.valid).toBeFalsy();
  });

  it('passwordConfirmation control should be invalid when empty', () => {
    let passwordConfirmation = component.passwordConfirmation!;
    passwordConfirmation.setValue('');
    expect(passwordConfirmation.valid).toBeFalsy();
  });

  it('form should be invalid when password and passwordConfirmation don\'t match', () => {
    component.password!.setValue('password');
    component.passwordConfirmation!.setValue('differentPassword');
    expect(component.registrationForm.valid).toBeFalsy();
  });

  it('form should be valid when all controls are valid', () => {
    component.email!.setValue('test@test.com');
    component.password!.setValue('password');
    component.passwordConfirmation!.setValue('password');
    expect(component.registrationForm.valid).toBeTruthy();
  });

  it('should emit nextStepEvent when nextStep is called and form is valid', () => {
    component.email!.setValue('test@test.com');
    component.password!.setValue('password');
    component.passwordConfirmation!.setValue('password');

    let emitted = false;
    component.nextStepEvent.subscribe((inputs) => {
      emitted = true;
      expect(inputs.step1Inputs).toEqual({
        email: 'test@test.com',
        password: 'password',
        passwordConfirmation: 'password'
      });
    });

    component.nextStep();
    expect(emitted).toBeTruthy();
  });


});
