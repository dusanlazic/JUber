import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';
import { ToastrModule } from 'ngx-toastr';
import { LoginPageComponent } from './login-page.component';
import { mockInvalidEmailErrorResponse, mockInvalidEmailFormatLoginRequest, mockInvalidEmailToastr, mockInvalidPasswordErrorResponse, mockInvalidPasswordLoginRequest, mockInvalidPasswordToastr, mockInvalidUserEmailLoginRequest, mockTokenResponse, mockValidLoginRequest} from 'src/mocks/auth.service.mock';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, of } from 'rxjs';


fdescribe('LoginPageComponent', () => {
  let component: LoginPageComponent;
  let fixture: ComponentFixture<LoginPageComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let toastr: jasmine.SpyObj<Toastr>;

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', ['login', 'handleSuccessfulAuth'])
    toastr = jasmine.createSpyObj('Toastr', ['error'])
    
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule,
        ToastrModule.forRoot(),
      ],
      providers: [{ provide: AuthService, useValue: authService },{provide: Toastr, useValue: toastr}, RouterTestingModule],
      declarations: [ LoginPageComponent ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(LoginPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should have an invalid form initially', () => {
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should have a disabled button initially', () =>{
    const loginButton = fixture.debugElement.nativeElement.querySelector('button[type="submit"]');
    expect(loginButton.disabled).toBeTruthy();
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should have a disabled button on invalid email format input', () =>{
    component.email?.setValue(mockInvalidEmailFormatLoginRequest.email);
    fixture.detectChanges();
    const loginButton = fixture.debugElement.nativeElement.querySelector('button[type="submit"]');
    expect(loginButton.disabled).toBeTruthy();
    expect(component.loginForm.invalid).toBeTruthy();
  })

  it('should have a enabled button on filled inputs', () =>{
    component.email?.setValue(mockValidLoginRequest.email);
    component.password?.setValue(mockValidLoginRequest.password);
    fixture.detectChanges();
    const loginButton = fixture.debugElement.nativeElement.querySelector('button[type="submit"]');
    expect(loginButton.disabled).toBeFalsy();
    expect(component.loginForm.valid).toBeTruthy();
  })

  
  it('should call login() when the form is submitted', () => {
    spyOn(component, 'login')

    component.email?.setValue(mockValidLoginRequest.email);
    component.password?.setValue(mockValidLoginRequest.password);
    fixture.detectChanges();

    const loginButton = fixture.debugElement.nativeElement.querySelector('button[type="submit"]');
    loginButton.click();

    expect(component.login).toHaveBeenCalled();
  });


  it('should call handleSuccessfulAuth after login with valid credentials', fakeAsync( () => {
    authService.login.and.returnValue(of(mockTokenResponse));

    component.email?.setValue(mockValidLoginRequest.email);
    component.password?.setValue(mockValidLoginRequest.password);
    fixture.detectChanges();
    
    component.login();
    tick()
    
    expect(authService.handleSuccessfulAuth).toHaveBeenCalled();
  }));

  it('should call handleLoginError after login with invalid password', fakeAsync(() => {
    spyOn(component, 'handleLoginError').and.callThrough()
    authService.login.and.returnValue(new Observable(observer => {observer.error(mockInvalidEmailErrorResponse)}))

    component.email?.setValue(mockInvalidUserEmailLoginRequest.email);
    component.password?.setValue(mockInvalidUserEmailLoginRequest.password);
    fixture.detectChanges();
    
    component.login();
    tick()
    expect(component.handleLoginError).toHaveBeenCalledWith(mockInvalidEmailErrorResponse.error)
    expect(toastr.error).toHaveBeenCalledWith(mockInvalidEmailToastr.message, mockInvalidEmailToastr.title);
  }));

  it('should call handleLoginError after login with non existing email', fakeAsync(() => {
    spyOn(component, 'handleLoginError').and.callThrough()
    authService.login.and.returnValue(new Observable(observer => {observer.error(mockInvalidPasswordErrorResponse)}))

    component.email?.setValue(mockInvalidPasswordLoginRequest.email);
    component.password?.setValue(mockInvalidPasswordLoginRequest.password);
    fixture.detectChanges();    

    component.login();
    tick()

    expect(component.handleLoginError).toHaveBeenCalledWith(mockInvalidPasswordErrorResponse.error);
    expect(toastr.error).toHaveBeenCalledWith(mockInvalidPasswordToastr.message, mockInvalidPasswordToastr.title);
  }));

});


