export interface DailyData {
    date: string;
    rides: number;
    distance: number;
    fare: number;
}

export interface Sums {
    rides: number;
    distance: number;
    fare: number;
}

export interface Averages {
    rides: number;
    distance: number;
    fare: number;
}

export interface ReportResponse {
    days: Array<DailyData>;
    sums: Sums;
    averages: Averages;
}
