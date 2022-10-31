package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Vehicle {

    @Id
    private UUID id;

    private Double longitude;

    private Double latitude;

    private Boolean babyFriendly;

    private Boolean petFriendly;

    private Integer capacity;

    @OneToOne
    private Driver driver;

}
