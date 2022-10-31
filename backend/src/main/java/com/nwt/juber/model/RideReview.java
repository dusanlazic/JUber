package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class RideReview {

    @Id
    private UUID id;

    @ManyToOne
    private Ride ride;

    private double driverRating;

    private double vehicleRating;

    private String comment;
}
