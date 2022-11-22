import { Point } from "./map"
import { Pal } from "./user"
import { VehicleType } from "./vehicle"

export class Place {
    name: string
    option: string
    point: Point | undefined
    editing: boolean
    
    constructor(name: string, option: string, point: Point | undefined = undefined) {
        this.name = name
        this.option = option
        this.point = point
        this.editing = false
    }
}


export class Ride {
    passengers: Array<Pal>
    places: Array<Place>
    price: number
    routes: Array<Array<Point>>
    routeNames: Array<string>

    constructor() {
        this.passengers = new Array<Pal>()
        this.places = new Array<Place>()
        this.price = 0
        this.routes = []
        this.routeNames = []
    }
}


export class AdditionalRequests {
    babyFriendly: boolean
    petFriendly: boolean
    vehicleType: VehicleType

    constructor() {
        this.babyFriendly = false
        this.petFriendly = false
        this.vehicleType = null
    }
}


export class RideRequest {
    ride: Ride
    requests: AdditionalRequests
    scheduleTime: string 

    constructor() {
        this.ride = new Ride()
        this.requests = new AdditionalRequests()
        this.scheduleTime = ''
    }
    
}