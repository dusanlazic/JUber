
export interface LoginRequest {
    email: string;
    password: string;
}

export interface TokenResponse {
    accessToken: string;
    expiresAt: number;
}

export interface PasswordResetLinkRequest {
    email: string;
}

export interface PasswordReset {
    password: string,
    passwordConfirmation: string,
    token: string
}


export interface AccountInfo {
    email: string,
    password: string,
    passwordConfirmation: string
}

export interface PersonalInfo {
    firstName: string,
    lastName: string,
    city: string,
    phoneNumber: string,
}

export interface LocalRegistrationInputs {
    step1Inputs?: AccountInfo,
    step2Inputs?: PersonalInfo
}

export interface LocalRegistrationRequest {
    email: string,
    password: string,
    passwordConfirmation: string

    firstName: string,
    lastName: string,
    city: string,
    phoneNumber: string
}