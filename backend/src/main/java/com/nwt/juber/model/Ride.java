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
    private UUID id = UUID.randomUUID();

    @OneToMany
    @JoinColumn(name = "ride_id")
    private List<Place> places;

    private Double fare;

    @ManyToMany
    private List<Passenger> passengers;

    @ElementCollection
    private List<PassengerStatus> passengersReady;

    @ManyToOne
    private Driver driver;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "RIDE_STATUS")
    private RideStatus rideStatus;

}
