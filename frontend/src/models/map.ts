export class Point {
    latitude: number;
    longitude: number;

    constructor(latitude: number, longitude: number) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

export class Route {
    points: [number, number][] = [];
    distance: number | undefined;
    time: number | undefined;
}