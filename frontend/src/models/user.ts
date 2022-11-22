export interface LoggedUser {
    id: string;
    name: string;
    email: string;
    imageUrl: string;
    role: string;
}


export interface AddPalEvent{
    confirmed: boolean,
    newPal?: Pal
}

export interface Pal {
    firstName?: string;
    lastName? : string;
    email: string;
    imageUrl?: string;
}

export enum Roles {
    PASSENGER="ROLE_PASSENGER",
    DRIVER="ROLE_DRIVER",
    PASSENGER_NEW="ROLE_PASSENGER_NEW"
}

