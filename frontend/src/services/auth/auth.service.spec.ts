import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { mockAdmin, mockApiResponse, mockDriver, mockInvalidEmailErrorResponse, mockInvalidPasswordErrorResponse, mockInvalidPasswordLoginRequest, mockInvalidSignupRequest, mockInvalidUserEmailLoginRequest, mockPassenger, mockSignupRequest, mockTokenResponse, mockUserAlreadyExistsErrorResponse, mockValidLoginRequest } from 'src/mocks/auth.service.mock';
import { Observable, of } from 'rxjs';
import { LoggedUser } from 'src/models/user';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';


fdescribe('AuthService_LoginTest', () => {
  let service: AuthService;
  let httpController: HttpTestingController;

  let mockRouter: { navigate: any; };

  let getCurrentUserSpy: jasmine.Spy<() => Observable<LoggedUser>>
  let onNewUserReceivedSpy: jasmine.Spy<(msg: LoggedUser | undefined) => void>

  let url = 'http://localhost:8080/auth';

  beforeEach(() => {
    mockRouter = { navigate: jasmine.createSpy('navigate') };
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [
        { provide: Router, useValue: mockRouter},
      ],
      teardown: {destroyAfterEach: false}
    }).compileComponents();
    httpController = TestBed.inject(HttpTestingController);
  });

  beforeEach(() => {
    // setup - no users logged
    getCurrentUserSpy = spyOn(AuthService.prototype, 'getCurrentUser').and.returnValue(new Observable)
    onNewUserReceivedSpy = spyOn(AuthService.prototype, 'onNewUserReceived')

    service = TestBed.inject(AuthService);

    expect(getCurrentUserSpy).toHaveBeenCalledTimes(1);
    expect(onNewUserReceivedSpy).toHaveBeenCalledTimes(0);
  });


  afterEach(() => {
      httpController.verify();
  });

  afterAll(() => {
    TestBed.resetTestingModule();
  });

  it('should call login with valid request and return tokenResponse', () => {
    service.login(mockValidLoginRequest).subscribe((res) => {
      expect(mockTokenResponse).toEqual(res)
    });
   
    const req = httpController.expectOne({method: 'POST', url: `${url}/login`,});
    req.flush(mockTokenResponse);
  });


  it('should call login with invalid password and return Unauthorized ErrorResponse', () => {
    service.login(mockInvalidPasswordLoginRequest).subscribe((res: any) => {
      expect(res).toEqual(mockInvalidPasswordErrorResponse);
    });
    const req = httpController.expectOne({method: 'POST', url: `${url}/login`});
    req.flush(mockInvalidPasswordErrorResponse);
  });

  it('should call login with non existing email and return Not Found ErrorResponse', () => {
    service.login(mockInvalidUserEmailLoginRequest).subscribe((res: any) => {
      expect(res).toEqual(mockInvalidEmailErrorResponse);
    });
    const req = httpController.expectOne({method: 'POST', url: `${url}/login`});
    req.flush(mockInvalidEmailErrorResponse);
  });



  it('should set logged passenger data on handleSuccessfulAuth call ', fakeAsync(() => {
    getCurrentUserSpy.and.returnValue(of(mockPassenger))
    
    service.handleSuccessfulAuth(mockTokenResponse.expiresAt)
    
    tick()
    expect(localStorage.getItem('tokenExpiration')).toEqual(mockTokenResponse.expiresAt.toString())
    expect(localStorage.getItem('role')).toEqual(mockPassenger.role)
    expect(getCurrentUserSpy).toHaveBeenCalled();
    expect(onNewUserReceivedSpy).toHaveBeenCalledWith(mockPassenger);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/home']);    
  }));

  it('should set logged driver data on handleSuccessfulAuth call ', fakeAsync(() => {
    getCurrentUserSpy.and.returnValue(of(mockDriver))
    
    service.handleSuccessfulAuth(mockTokenResponse.expiresAt)
    
    tick()
    expect(localStorage.getItem('tokenExpiration')).toEqual(mockTokenResponse.expiresAt.toString())
    expect(localStorage.getItem('role')).toEqual(mockDriver.role)
    expect(getCurrentUserSpy).toHaveBeenCalled();
    expect(onNewUserReceivedSpy).toHaveBeenCalledWith(mockDriver);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/ride']);    
  }));

  it('should set logged admin data on handleSuccessfulAuth call ', fakeAsync(() => {
    getCurrentUserSpy.and.returnValue(of(mockAdmin))
    
    service.handleSuccessfulAuth(mockTokenResponse.expiresAt)
    
    tick()
    expect(localStorage.getItem('tokenExpiration')).toEqual(mockTokenResponse.expiresAt.toString())
    expect(localStorage.getItem('role')).toEqual(mockAdmin.role)
    expect(getCurrentUserSpy).toHaveBeenCalled();
    expect(onNewUserReceivedSpy).toHaveBeenCalledWith(mockAdmin);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/admin']);    
  }));


  it('should call signup with valid request and return apiresponse', () => {
    service.signup(mockSignupRequest).subscribe((res) => {
      expect(mockApiResponse).toEqual(res)
    });
   
    const req = httpController.expectOne({method: 'POST', url: `${url}/register`,});
    req.flush(mockApiResponse);
  });


  it('should call login with invalid password and return Unauthorized ErrorResponse', () => {
    service.signup(mockInvalidSignupRequest).subscribe((res: any) => {
      expect(mockUserAlreadyExistsErrorResponse).toEqual(res)
    });
   
    const req = httpController.expectOne({method: 'POST', url: `${url}/register`,});
    req.flush(mockUserAlreadyExistsErrorResponse);
  });
});
