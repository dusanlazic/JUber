import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RegisterStep2Component } from './register-step2.component';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { Toastr } from 'src/services/util/toastr.service';

describe('RegisterStep2Component', () => {
  let component: RegisterStep2Component;
  let fixture: ComponentFixture<RegisterStep2Component>;
  let toastr: Toastr;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ ReactiveFormsModule, ToastrModule.forRoot() ],
      declarations: [ RegisterStep2Component ],
      providers: [FormBuilder, Toastr],
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterStep2Component);
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

  it('should be invalid when first name control is empty', () => {
    let firstName = component.firstName!;
    firstName.setValue('');
    expect(firstName.valid).toBeFalsy();
  });

  it('should be invalid when last name control is empty', () => {
    let lastName = component.lastName!;
    lastName.setValue('');
    expect(lastName.valid).toBeFalsy();
  });

  it('should be invalid when city control is empty', () => {
    let city = component.city!;
    city.setValue('');
    expect(city.valid).toBeFalsy();
  });

  it('should be invalid when phone number control is empty', () => {
    let phoneNumber = component.phoneNumber!;
    phoneNumber.setValue('');
    expect(phoneNumber.valid).toBeFalsy();
  });

  it('should be invalid when phone number is not in the valid format', () => {
    let phoneNumber = component.phoneNumber!;
    phoneNumber.setValue('12345');
    expect(phoneNumber.valid).toBeFalsy();
  });

  it('should be valid when phone number is in the valid format', () => {
    let phoneNumber = component.phoneNumber!;
    phoneNumber.setValue('+650001337');
    expect(phoneNumber.valid).toBeTruthy();
  });

  it('should have valid form when all controls are valid', () => {
    component.firstName?.setValue("Pera");
    component.lastName?.setValue("Peric")
    component.city?.setValue("Novi Sad");
    component.phoneNumber?.setValue("+650001337");

    expect(component.registrationForm.valid).toBeTruthy();
  });

  it('should emit nextStepEvent when nextStep is called and form is valid', () => {
    component.firstName?.setValue("Pera");
    component.lastName?.setValue("Peric")
    component.city?.setValue("Novi Sad");
    component.phoneNumber?.setValue("+650001337");

    let emitted = false;
    component.nextStepEvent.subscribe((inputs) => {
      emitted = true;
      expect(inputs.step2Inputs).toEqual({
        firstName: 'Pera',
        lastName: 'Peric',
        city: 'Novi Sad',
        phoneNumber: '+650001337'
      });
    });

    component.nextStep();
    expect(emitted).toBeTruthy();
  });
});
