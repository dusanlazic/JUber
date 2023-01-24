export interface LoggedUser {
    id: string;
    name: string;
    email: string;
    imageUrl: string;
    role: string;
    provider: AuthProvider
}

export enum AuthProvider {
    local="local", 
    facebook="facebook", 
    google="google"
}

export enum Roles {
    PASSENGER="ROLE_PASSENGER",
    DRIVER="ROLE_DRIVER",
    PASSENGER_NEW="ROLE_PASSENGER_NEW",
    ADMIN="ROLE_ADMIN"
}

export interface UserBasicInfo {
    firstName: string;
    lastName: string;
    email: string;
    imageUrl: string;
}

export interface AccountInfo extends UserBasicInfo{
    city: string,
    phoneNumber: string,
}

export interface PersonDTO extends AccountInfo {
    id: string;
}