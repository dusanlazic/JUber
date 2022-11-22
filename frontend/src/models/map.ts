import { IPoint } from "src/app/store/ride";

export class Point implements IPoint{
    latitude: number;
    longitude: number;

    constructor(latitude: number, longitude: number) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}