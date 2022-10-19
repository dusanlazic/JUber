package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
public class RideCancellation {

    @Id
    private Long id;

    private String reason;

    @OneToOne
    private Ride ride;

}
