import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { mockInvalidPasswordErrorResponse, mockInvalidPasswordLoginRequest, mockTokenResponse, mockValidLoginRequest } from 'src/mocks/auth.service.mock';

fdescribe('AuthService', () => {
  let service: AuthService;
  let httpController: HttpTestingController;

  let url = 'http://localhost:8080/auth';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
    }).compileComponents();
    service = TestBed.inject(AuthService);
    httpController = TestBed.inject(HttpTestingController);
  });


  afterEach(() => {
    httpController.verify();
  });

  it('should call login with valid request and return tokenResponse', () => {
    service.login(mockValidLoginRequest).subscribe((res) => {
      expect(res).toEqual(mockTokenResponse);
    });
    const req = httpController.expectOne({
      method: 'POST',
      url: `${url}/login`,
    });
    req.flush(mockTokenResponse);
    const req2 = httpController.expectOne({
      method: 'GET',
      url: `${url}/me`,
    });
    req2.flush(mockTokenResponse.expiresAt)
  });


  it('should call login with invalid password and return ErrorResponse', () => {
    service.login(mockInvalidPasswordLoginRequest).subscribe({
      error(err) {
        expect(err).toEqual(mockInvalidPasswordErrorResponse);
      },
    });
    const req = httpController.expectOne({
      method: 'POST',
      url: `${url}/login`,
    });
    req.flush(mockTokenResponse);
    const req2 = httpController.expectOne({
      method: 'GET',
      url: `${url}/me`,
    });
    req2.flush(mockTokenResponse.expiresAt)
  });

});

