export interface IPoint {
    latitude: number;
    longitude: number;
}

export interface IRoute {
    name: string
    distance: number
    duration: number
    selected: boolean
    coordinates: Array<IPoint>
    coordinatesEncoded: string
    getCoordinates(): Array<IPoint>
}

export interface IPlace {
    name: string
    option: string
    point: IPoint | undefined
    editing: boolean
    routes: Array<IRoute>

}

export interface IRide {
    passengers: Array<string>
    places: Array<IPlace>
    fare: number
}