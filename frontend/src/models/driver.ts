import { UserBasicInfo } from "./user";

export enum DriverStatus {
    ACTIVE="ACTIVE",
	INACTIVE="INACTIVE",
	OVERTIME="OVERTIME",
	DRIVING="DRIVING"
}

export interface DriverActivationResponse {

    status: DriverStatus,
    overtimeEnd: Date;
}

export interface DriverRegistrationRequest {
    email: string;
    password: string;
    passwordConfirmation: string;
    firstName: string;
    lastName: string;
    city: string;
    phoneNumber: string;
    babyFriendly: boolean;
    petFriendly: boolean;
    capacity: number;
}

export interface DriverInfo {
    profile: PersonInfo;
    status: DriverStatus;
}

export interface PersonInfo {
    firstName: string;
    lastName: string;
    email: string;
    imageUrl: string;
    city: string;
    phoneNumber: string;
}