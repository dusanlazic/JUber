export interface IPoint {
    latitude: number;
    longitude: number;
}

export interface IRoute {
    name: string
    distance: number
    time: number
    selected: boolean
    coordinates: Array<IPoint>
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