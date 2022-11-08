
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