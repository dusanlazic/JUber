
export type LoginRequest = {
    email: string;
    password: string;
}

export type TokenResponse = {
    accessToken: string;
    expiresAt: number;
}

export type PasswordResetLinkRequest = {
    email: string;
}

export type PasswordReset = {
    password: string,
    passwordConfirmation: string,
    token: string
}


export type AccountInfo = {
    email: string,
    password: string,
    passwordConfirmation: string
}

export type PersonalInfo = {
    firstName: string,
    lastName: string,
    city: string,
    phoneNumber: string,
}

export type LocalRegistrationInputs = {
    step1Inputs?: AccountInfo,
    step2Inputs?: PersonalInfo
}

export type LocalRegistrationRequest = {
    email: string,
    password: string,
    passwordConfirmation: string

    firstName: string,
    lastName: string,
    city: string,
    phoneNumber: string
}