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