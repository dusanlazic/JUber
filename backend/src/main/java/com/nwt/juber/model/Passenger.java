package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Passenger extends Person {

    @ManyToMany
    private List<Ride> favouriteRoutes;

    @OneToMany(mappedBy = "passenger")
    private List<DepositAddress> depositAddresses;

    private BigDecimal balance = BigDecimal.ZERO;


    @Override
    public String toString() {
        return "Passenger{" +
                "favouriteRoutes=" + favouriteRoutes +
                ", balance=" + balance +
                '}';
    }
}
