package com.nwt.juber.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    private String name;

    private String option;

    @OneToMany
    @JoinColumn(name = "place_id")
    private List<Route> routes;

    private Double latitude;

    private Double longitude;

    public Place(String name, String option, List<Route> routes, Double latitude, Double longitude) {
        this.name = name;
        this.option = option;
        this.routes = routes;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
