import { Ride, RideStatus } from "./ride";

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
    startLocationName: string;
    response: NotificationResponse;
    notificationId: string;
}

export interface RideStatusUpdatedNotification  extends TransferredNotification {
    status: RideStatus,
}

export interface RideReminderNotification  extends TransferredNotification {
    minutesLeft: number;
}

export interface EveryoneAcceptedRideNotification extends TransferredNotification {
    untilDriverArival: number;
}

export interface TransferredNotification {
    date: Date;     // "2023-01-13T14:13:07.355+00:00"
    type: NotificationType;
    notificationStatus: NotificationStatus;
}

export type Notification = 
    DriverArrivedNotification | 
    NewRideAssignedNotification |
    RideCancelledNotification | 
    RideInvitationNotification | 
    RideStatusUpdatedNotification | 
    RideReminderNotification |
    EveryoneAcceptedRideNotification

export enum NotificationType {
    DRIVER_ARRIVED='DRIVER_ARRIVED',
    RIDE_ASSIGNED='RIDE_ASSIGNED',
    RIDE_INVITATION='RIDE_INVITATION',
    RIDE_REMINDER='RIDE_REMINDER',
    RIDE_STATUS_UPDATED='RIDE_STATUS_UPDATED',
    RIDE_CANCELLED='RIDE_CANCELLED',
    RIDE_ACCEPTED='RIDE_ACCEPTED'
}

export enum NotificationStatus {
    READ='READ',
    UNREAD='UNREAD'
}

export enum NotificationResponse {
    ACCEPTED='ACCEPTED',
	DECLINED='DECLINED',
	NO_RESPONSE='NO_RESPONSE'
}

export interface NotificationItemProperties{
    iconClass: string,
    icon: string,
    message: string
  }