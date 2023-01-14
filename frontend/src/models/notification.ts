export interface DriverArrivedNotification extends TransferredNotification {
    driverImageUrl: string;
    driverName: string;
}

export interface NewRideAssignedNotification extends TransferredNotification {
    passengerCount: number;
    startLocationName: string;
}

export interface RideCancelledNotification  extends TransferredNotification {
    canceler: string;
    cancelerImageUrl: string;
    rideId: string;
}

export interface RideInvitationNotification  extends TransferredNotification {
    inviterName: string;
    inviterImageUrl: string;
    rideId: string;
    balance: number;
}

export interface RideStatusUpdatedNotification  extends TransferredNotification {
    status: string //RideStatus;
}

export interface RideReminderNotification  extends TransferredNotification {
    minutesLeft: number;
}

export interface TransferredNotification {
    date: Date;     // "2023-01-13T14:13:07.355+00:00"
    type: NotificationType;
}

export type Notification = 
    DriverArrivedNotification | 
    NewRideAssignedNotification |
    RideCancelledNotification | 
    RideInvitationNotification | 
    RideStatusUpdatedNotification | 
    RideReminderNotification

export enum NotificationType {
    DRIVER_ARRIVED='DRIVER_ARRIVED',
    RIDE_ASSIGNED='RIDE_ASSIGNED',
    RIDE_INVITATION='RIDE_INVITATION',
    RIDE_REMINDER='RIDE_REMINDER',
    RIDE_STATUS_UPDATED='RIDE_STATUS_UPDATED',
    RIDE_CANCELLED='RIDE_CANCELLED'
}