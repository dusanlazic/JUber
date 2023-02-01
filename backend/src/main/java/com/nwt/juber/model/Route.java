package com.nwt.juber.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    private String name;

    private Double distance;

    private Double duration;

    private String coordinatesEncoded;

    private Boolean selected;

    public Route(String name, Double distance, Double duration, String coordinatesEncoded, Boolean selected) {
        this.name = name;
        this.distance = distance;
        this.duration = duration;
        this.coordinatesEncoded = coordinatesEncoded;
        this.selected = selected;
    }
}
