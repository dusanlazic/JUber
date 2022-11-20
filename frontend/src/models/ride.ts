import { Point } from "./map"

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
    passengers: Array<string>
    places: Array<Place>
    price: number
    routes: Array<Array<Point>>
    routeNames: Array<string>

    constructor() {
        this.passengers = new Array<string>()
        this.places = new Array<Place>()
        this.price = 0
        this.routes = []
        this.routeNames = []
    }
}