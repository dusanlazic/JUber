package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Ride {

    @Id
    private Long id;

    @ManyToOne
    private Route route;

    private Double fare;

    @ManyToMany
    private List<Passenger> passengers;

    @ManyToOne
    private Driver driver;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private RideStatus rideStatus;

}
