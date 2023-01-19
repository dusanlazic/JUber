package com.nwt.juber.model;

public enum RideStatus {
    WAITING_FOR_PAYMENT, // waiting for passengers to pay
    WAIT, // waiting for driver to be assigned
    ACCEPTED, // driver is heading to location
    IN_PROGRESS, // passengers are in the car
    DENIED, // ride didn't successfully finish
    FINISHED, // ride successfully finished
    SCHEDULED // driver has accepted that he will drive the ride in the future
}
