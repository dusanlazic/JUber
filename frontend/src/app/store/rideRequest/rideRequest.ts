import { Ride, Route } from "src/models/ride"
import { AdditionalRequests } from "src/models/rideRequest"

export type VehicleType = IVehicleType | null

export interface IVehicleType {
    name: string,
    price: number
}

export interface IAdditionalRequest {
    babyFriendly: boolean
    petFriendly: boolean
    vehicleType: VehicleType
}

export interface AddPalEvent{
    confirmed: boolean,
    newPal?: IPal
}

export interface IPal {
    firstName?: string;
    lastName? : string;
    email: string;
    imageUrl?: string;
}


export interface IRideRequest {
    ride: Ride
    additionalRequests: AdditionalRequests
    scheduleTime: string 
    passengersInfo: IPal[]
    passengerEmails: string[]
}
