import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { Observable, of } from 'rxjs';
import { mockApiResponse, mockInvalidStep1Inputs, mockSignupInputs, mockSignupRequest, mockStep1Inputs, mockStep2Inputs, mockUserAlreadyExistsErrorResponse } from 'src/mocks/auth.service.mock';
import { AccountInfo, LocalRegistrationInputs, LocalRegistrationRequest, PersonalInfo } from 'src/models/auth';
import { ApiResponse } from 'src/models/responses';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';

import { RegisterLocalComponent, RegistrationStep } from './register-local.component';
import { RegisterStep1Component } from './register-step1/register-step1.component';
import { RegisterStep2Component } from './register-step2/register-step2.component';

fdescribe('RegisterLocalComponent', () => {
  let component: RegisterLocalComponent;
  let fixture: ComponentFixture<RegisterLocalComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let toastr: jasmine.SpyObj<Toastr>;

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', ['signup'])
    toastr = jasmine.createSpyObj('Toastr', ['error'])

    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule,
        ToastrModule.forRoot(),
      ],
      providers: [{ provide: AuthService, useValue: authService },{provide: Toastr, useValue: toastr}],
      declarations: [ RegisterLocalComponent, RegisterStep2Component, RegisterStep1Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterLocalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be first step active on init', () => {
    expect(component.activeStep).toEqual(RegistrationStep.FIRST_STEP);
  });


  it('should show second step active after change', () => {
    component.changeStep(RegistrationStep.SECOND_STEP, mockSignupInputs);

    expect(component.activeStep).toEqual(RegistrationStep.SECOND_STEP);
    expect(component.step1Inputs).toEqual(mockSignupInputs.step1Inputs!);
  });

  it('should call sendRegistrationRequest if step changed to success', () => {
    spyOn(component, 'sendRegistrationRequest')
    component.changeStep(RegistrationStep.SUCCESS, mockSignupInputs);

    expect(component.step2Inputs).toEqual(mockSignupInputs.step2Inputs!);
    expect(component.sendRegistrationRequest).toHaveBeenCalled()
  });

  it('should show success step if registration successful', fakeAsync(() => {
  
    authService.signup.and.returnValue(of(mockApiResponse));
    component.step1Inputs = mockStep1Inputs;
    component.step2Inputs = mockStep2Inputs;

    component.sendRegistrationRequest()

    expect(authService.signup).toHaveBeenCalledWith(mockSignupRequest)
    expect(component.activeStep).toEqual(RegistrationStep.SUCCESS);
  }));

  it('should call handleRegistrationError after signup with already existing email', fakeAsync(() => {
    spyOn(component, 'handleRegistrationError').and.callThrough()
    authService.signup.and.returnValue(new Observable(observer => {observer.error(mockUserAlreadyExistsErrorResponse)}))
 
    component.step1Inputs = mockInvalidStep1Inputs;
    component.step2Inputs = mockStep2Inputs;

    component.sendRegistrationRequest()

    expect(component.handleRegistrationError).toHaveBeenCalledWith(mockUserAlreadyExistsErrorResponse.error);
    expect(toastr.error).toHaveBeenCalledWith(mockUserAlreadyExistsErrorResponse.error.message);
  }));

});