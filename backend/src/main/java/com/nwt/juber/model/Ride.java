package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Ride {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    private Route route;

    private Double fare;

    @ManyToMany
    private List<Passenger> passengers;

    @ManyToOne
    private Driver driver;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "RIDE_STATUS")
    private RideStatus rideStatus;

}
