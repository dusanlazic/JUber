import { IAdditionalRequest, IRideRequest, IPal, VehicleType } from "src/app/store/rideRequest/rideRequest"
import { Ride, Route } from "./ride"


export class AdditionalRequests implements IAdditionalRequest {
    constructor(
        public babyFriendly: boolean = false,
        public petFriendly: boolean = false,
        public vehicleType: VehicleType = null,) {
    }
}



export class RideRequest implements IRideRequest {
    constructor(
        public ride: Ride = new Ride(),
        public additionalRequests: AdditionalRequests = new AdditionalRequests(),
        public scheduleTime: string = '',
        public passengersInfo: IPal[] = new Array<IPal>(),
        public passengerEmails: string[]= new Array<string>()) {
    }
}
