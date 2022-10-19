package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
public class Person extends User {

    private String name;

    private String surname;

    private String city;

    private String phoneNumber;

    @OneToOne
    private PaymentInfo paymentInfo;

    private String photoPath;

    private boolean active;
}
