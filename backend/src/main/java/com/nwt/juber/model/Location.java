package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Location {

    @Id
    private Long id;

    private String name;

    private Double longitude;

    private Double latitude;

}
