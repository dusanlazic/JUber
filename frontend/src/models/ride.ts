import { Point } from "./map"
import { IRoute, IPlace, IPoint, IRide } from "src/app/store/ride"


export class Route implements IRoute {
    constructor(public name: string = "",
        public distance: number = -1,
        public time: number = -1,
        public selected: boolean = false,
        public coordinates: IPoint[] = []) {

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


export enum RideStatus {
    WAITING_FOR_PAYMENT='WAITING_FOR_PAYMENT', // waiting for passengers to pay
    WAIT='WAIT', // waiting for driver to be assigned
    ACCEPTED='ACCEPTED', // driver is heading to location
    IN_PROGRES='IN_PROGRESS', // passengers are in the car
    DENIED='DENIED', // ride didn't successfully finish
    FINISHED='FINISHED' // ride successfully finished
}