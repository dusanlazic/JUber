package com.nwt.juber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @OneToMany
    @JoinColumn(name = "ride_id")
    private List<Place> places;

    private Double fare;

    private Boolean babyFriendlyRequested;

    private Boolean petFriendlyRequested;

    @ManyToOne
    private VehicleType vehicleTypeRequested;

    @ManyToMany
    private List<Passenger> passengers;

    @ElementCollection
    private List<PassengerStatus> passengersReady;

    @ManyToMany
    private List<Driver> blacklisted;

    @ManyToOne
    private Driver driver;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "RIDE_STATUS")
    private RideStatus rideStatus;

    @Column(name = "SCHEDULED_TIME")
    private LocalDateTime scheduledTime;

    @Column(name = "ESTIMATED_TIME")
    private Integer duration;

    @Column(name = "DISTANCE")
    private Double distance;

    public Ride(List<Place> places, Double fare, Integer duration, Double distance) {
        this.places = places;
        this.fare = fare;
        this.duration = duration;
        this.distance = distance;
    }
}
