import { HttpStatusCode } from '@angular/common/http';
import { LoginRequest, TokenResponse } from 'src/models/auth';
import { AuthProvider, LoggedUser, Roles } from 'src/models/user';

const mockTokenResponse: TokenResponse =  {
    accessToken: 'accessToken',
    expiresAt: 100000000
};

const mockValidLoginRequest: LoginRequest = {
    email: 'mile.miletic@gmail.com',
    password: 'cascaded'
}

const mockInvalidPasswordLoginRequest: LoginRequest = {
    email: 'petar.petrovic@gmail.com',
    password: 'cascaded1'
}

const mockInvalidUserEmailLoginRequest: LoginRequest = {
    email: 'petar@gmail.com',
    password: 'cascaded1'
}

const mockInvalidEmailFormatLoginRequest: LoginRequest = {
    email: 'petar',
    password: 'cascaded1'
}

const mockInvalidPasswordErrorResponse = { status: HttpStatusCode.Unauthorized, statusText: 'Unauthorized', 
        error: {status: HttpStatusCode.Unauthorized, message: "Invalid password"} };


const mockInvalidEmailErrorResponse = { status: HttpStatusCode.NotFound, statusText: 'Not Found', 
        error: {status: HttpStatusCode.Unauthorized, message: "User not found"} };


const mockInvalidPasswordToastr = {
    message: 'Make sure you have an activated account.', 
    title: mockInvalidPasswordErrorResponse.error.message 
}

const mockInvalidEmailToastr = {
    message: 'Make sure you have an activated account.', 
    title: mockInvalidEmailErrorResponse.error.message
}


const mockPassenger: LoggedUser = {
    email: 'mile.miletic@gmail.com', 
    id: '92348c29-e3cb-4c8f-ad5c-f31bf14db84d',
    imageUrl: '',
    name: 'Mile Miletic',
    provider: AuthProvider.local,
    role: Roles.PASSENGER
}
const mockDriver: LoggedUser = {
    email: 'zdravko.zdravkovic@gmail.com', 
    id: '909dccc3-4f61-4237-b3a2-6e674edd8d52',
    imageUrl: '',
    name: 'Zdravko Zdravkovic',
    provider: AuthProvider.local,
    role: Roles.DRIVER
}
const mockAdmin: LoggedUser = {
    email: 'admin@juber.com', 
    id: 'e3661c31-d1a4-47ab-94b6-1c6500dccf24',
    imageUrl: '',
    name: 'JUber Admin',
    provider: AuthProvider.local,
    role: Roles.ADMIN
}

export { mockTokenResponse, mockValidLoginRequest, 
        mockInvalidPasswordLoginRequest, mockInvalidUserEmailLoginRequest, mockInvalidEmailFormatLoginRequest, 
        mockInvalidPasswordErrorResponse, mockInvalidEmailErrorResponse,
        mockInvalidPasswordToastr, mockInvalidEmailToastr,
        mockPassenger, mockDriver, mockAdmin
     };