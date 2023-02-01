package com.nwt.juber.model;

import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Driver extends Person {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ride> rides;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Vehicle vehicle;
    
    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DriverShift> driverShifts;


    @Override
    public String toString() {
        return "Driver{" +
                "vehicle=" + vehicle +
                ", status=" + status +
                ", driverShifts=" + driverShifts +
                '}';
    }
}
