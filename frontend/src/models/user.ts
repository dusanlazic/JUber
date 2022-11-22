export interface LoggedUser {
    id: string;
    name: string;
    email: string;
    imageUrl: string;
    role: string;
}

export interface Pal {
    firstName: string;
    lastName: string;
    email: string;
    imageUrl?: string;
}

export interface PalInput {
    email: string;
    name: string;
    firstName: string;
    lastName: string;
    imageUrl?: string;
}


export enum Roles {
    PASSENGER="ROLE_PASSENGER",
    DRIVER="ROLE_DRIVER",
    PASSENGER_NEW="ROLE_PASSENGER_NEW"
}

