export interface LoggedUser {
    id: string;
    name: string;
    email: string;
    imageUrl: string;
    role: string;
}

export enum Roles {
    PASSENGER="ROLE_PASSENGER",
    DRIVER="ROLE_DRIVER",
    PASSENGER_NEW="ROLE_PASSENGER_NEW"
}

