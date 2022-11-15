
export type LoginRequest = {
    email: string;
    password: string;
}

export type TokenResponse = {
    accessToken: string;
}

export type PasswordResetLinkRequest = {
    email: string;
}


export type RegistrationStep1 = {
    email: string,
    password: string,
    passwordConfirmation: string
}

export type RegistrationStep2 = {
    firstName: string,
    lastName: string,
    city: string,
    phoneNumber: string,
}

export type LocalRegistrationInputs = {
    step1Inputs?: RegistrationStep1,
    step2Inputs?: RegistrationStep2
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