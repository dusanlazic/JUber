package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Driver extends Person {

    @OneToMany
    private List<Ride> rides;

    @OneToOne
    private Vehicle vehicle;

}
