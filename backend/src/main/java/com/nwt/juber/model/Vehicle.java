package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Vehicle {

    @Id
    private Long id;

    @ManyToOne
    private Location location;

    private Boolean babyFriendly;

    private Boolean petFriendly;

}
