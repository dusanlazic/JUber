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