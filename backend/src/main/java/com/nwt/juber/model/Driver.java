package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Driver extends Person {

    @OneToMany
    private List<Ride> rides;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Vehicle vehicle;
    
    private Boolean active;

}
