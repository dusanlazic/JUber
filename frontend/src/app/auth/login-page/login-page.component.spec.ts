import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';
import { ToastrModule } from 'ngx-toastr';
import { LoginPageComponent } from './login-page.component';
import { AuthServiceMock, mockInvalidEmailFormatLoginRequest, mockTokenResponse, mockValidLoginRequest } from 'src/mocks/auth.service.mock';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable } from 'rxjs';


fdescribe('LoginPageComponent', () => {
  let component: LoginPageComponent;
  let fixture: ComponentFixture<LoginPageComponent>;
  let authService: AuthServiceMock;
  let toastr: Toastr;
  beforeEach(async () => {
    
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule,
        ToastrModule.forRoot()
      ],
      providers: [{ provide: AuthService, useClass: AuthServiceMock }, Toastr, RouterTestingModule],
      declarations: [ LoginPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    
    authService = TestBed.get(AuthService);
    toastr = TestBed.get(Toastr);
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

  it('should have a disabled button on invalid email input', () =>{
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
    component.email?.setValue(mockValidLoginRequest.email);
    component.password?.setValue(mockValidLoginRequest.password);
    fixture.detectChanges();
    spyOn(component, 'login')

    const loginButton = fixture.debugElement.nativeElement.querySelector('button[type="submit"]');
    loginButton.click();
    expect(component.login).toHaveBeenCalled();
  });

  it('should call authService.login() after login() is called', () => {
    component.email?.setValue(mockValidLoginRequest.email);
    component.password?.setValue(mockValidLoginRequest.password);
    fixture.detectChanges();
    spyOn(authService, 'login').and.returnValue(new Observable(observer => {observer.next(mockTokenResponse)}))
    let handleSuccessfulAuthSpy = spyOn(authService, "handleSuccessfulAuth");

    component.login();

    fixture.whenStable().then(()=>{
      expect(handleSuccessfulAuthSpy).toHaveBeenCalled();
    })    
  });
});


