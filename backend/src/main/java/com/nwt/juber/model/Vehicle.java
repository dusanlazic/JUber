package com.nwt.juber.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"driver"})
@NoArgsConstructor
public class Vehicle {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private Double longitude;

    private Double latitude;

    private Boolean babyFriendly;

    private Boolean petFriendly;

    private Integer capacity;
    
    @OneToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Driver driver;

}
