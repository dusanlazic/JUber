package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Place {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    private String option;

    @OneToMany
    @JoinColumn(name = "place_id")
    private List<Route> routes;

    private Double latitude;

    private Double longitude;

}
