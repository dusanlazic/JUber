import { NotificationItemProperties } from "src/models/notification"

enum Properties {
  CHECK_ICON = 'fluent:checkmark-12-filled',
  SUCCESS_CLASS = 'has-background-success',

  SAD_EMOJI_ICON = 'fluent:emoji-sad-16-regular',
  FAIL_CLASS = 'has-background-danger',

  CAR_ICON = 'mdi:car-side',
  INFO_CLASS = 'has-background-info',

  BELL_ICON = 'fluent:alert-24-filled',
  WARNING_CLASS = 'has-background-warning'
}

export class NotificationTemplate {

  // to passenger - on RideStatusUpdatedNotification notif
  static rideAccepted(arrivalTimeInMinutes: number): NotificationItemProperties {
    return {
      icon: Properties.CHECK_ICON, 
      iconClass: Properties.SUCCESS_CLASS,
      message: `Ride accepted! Your driver will be there in about <b>${arrivalTimeInMinutes} minutes</b>`
    }
  }

  // to passenger - on RideStatusUpdatedNotification notif
  static rideRejected(): NotificationItemProperties {
    return {
      icon: Properties.SAD_EMOJI_ICON, 
      iconClass: Properties.FAIL_CLASS,
      message: 'Ride rejected! There are no available drivers right now. Try again later, or schedule a ride for later.'
    }
  }

  // to passenger - on DriverArrivedNotification notif
  static driverArived(driverName: string): NotificationItemProperties {
    return {
      icon: Properties.CAR_ICON, 
      iconClass: Properties.INFO_CLASS,
      message: `Your driver <b>${driverName}</b> has arrived!`
    }
  } 

  // to driver? - on NewRideAssignedNotification notif
  static rideAssigned(startLocationName: string): NotificationItemProperties {
    return {
      icon: Properties.CAR_ICON, 
      iconClass: Properties.INFO_CLASS,
      message: `You've got a new ride! Starting location: <b>${startLocationName}</b>!`
    }
  }

  // to passenger - on RideCancelledNotification notif - one of the passengers has cancelled?
  static rideCancelled(cancelerName: string): NotificationItemProperties {
    return {
      icon: Properties.SAD_EMOJI_ICON, 
      iconClass: Properties.FAIL_CLASS,
      message: `Your pal <b>${cancelerName}</b> has denclined a ride.`
    }
  }

  // to passenger - on RideReminderNotification notif
  static rideReminder(minutesLeft: number): NotificationItemProperties {
    return {
      icon: Properties.BELL_ICON, 
      iconClass: Properties.WARNING_CLASS,
      message: `Ride reminder! Your driver will be there in about <b>${minutesLeft} minutes</b>`
    }
  }
}



export class NotificationTimestampUtil {

  constructor() {}
  
  static getTimestampText(notificationDate: Date): string {
      const timeDifference = new Date().getTime() - new Date(notificationDate).getTime();
      const minutes = Math.floor((timeDifference / (1000 * 60)) % 60);
      const hours = Math.floor((timeDifference / (1000 * 60 * 60)) );
      if(hours > 24) {
        return 'Yesterday';
      }
      if(hours > 0){
        return `${hours}h ago`;
      }
      if(minutes > 0){
        return `${minutes}m ago`;
      }

      return 'Just now';
  }
}