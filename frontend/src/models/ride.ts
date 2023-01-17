import { IPal, VehicleType } from "src/app/store/rideRequest/rideRequest"
import { Point } from "./map"
import { IRoute, IPlace, IPoint, IRide } from "src/app/store/ride"
import { decode, encode } from "@googlemaps/polyline-codec";

export class Route implements IRoute {
    constructor(public name: string = "",
        public distance: number = -1,
        public time: number = -1,
        public selected: boolean = false,
        public coordinates: IPoint[] = [],
        public coordinatesEncoded: string = "") {

    }

    getCoordinates(): IPoint[] {
        if (this.coordinatesEncoded) {
            this.coordinates = decode(this.coordinatesEncoded).map(x => new Point(x[0], x[1]));
        }
        return this.coordinates;
    }
    
}

export class Place implements IPlace {
    constructor(public name: string = "",
                public id: number = -1,
                public option: string = "",
                public point: Point | undefined = undefined,
                public editing: boolean = false,
                public routes: Array<Route> = []) {
    }
}

export class Ride implements IRide {
    constructor(public passengers: Array<string> = [],
        public places: Array<Place> = [],
        public fare: number = -1) {
    }
}

export interface IPerson {
    firstName?: string;
    lastName? : string;
    email: string;
    imageUrl?: string;
    status? : string;
    id?: string;
}

export class FullRide {
    constructor(
        // TODO: placedto
        public places: Array<Place> = [],
        public fare: number = -1,
        public babyFriendly: boolean = false,
        public petFriendly: boolean = false,
        public vehicleType: VehicleType = null,
        public passengers: IPerson[] = new Array<IPerson>(),
        public passengersReady: string[] = [],
        public driver: IPerson | undefined = undefined,
        public rideStatus: string = '',
        public id: string = '') {
            for(let [i, passenger] of passengers.entries()) {
                passenger.status = passengersReady[i];
            }
    }

    public setStatus() {
        for(let [i, passenger] of this.passengers.entries()) {
            passenger.status = this.passengersReady[i];
        }
    }
}



export enum RideStatus {
    WAITING_FOR_PAYMENT='WAITING_FOR_PAYMENT', // waiting for passengers to pay
    WAIT='WAIT', // waiting for driver to be assigned
    ACCEPTED='ACCEPTED', // driver is heading to location
    IN_PROGRES='IN_PROGRESS', // passengers are in the car
    DENIED='DENIED', // ride didn't successfully finish
    FINISHED='FINISHED' // ride successfully finished
}