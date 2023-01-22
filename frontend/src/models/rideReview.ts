export interface RideReviewRequest extends RideReviewInput {
    rideId: string;
}

export interface RideReviewInput {
    driverRating: number;
    vehicleRating: number;
    comment: string;
}

export interface RideReviewInputEvent {
    rideReviewInput?: RideReviewInput;
    confirmed: boolean,
}

export interface RideReview {
    reviewerFullName: string;
    reviewerImageUrl: string;
    driverRating: number;
    vehicleRating : number;
    comment: string;
}

export interface ReviewableInfo {
    deadline: Date;
    alreadyReviewed: boolean;
    deadlinePassed: boolean;
}