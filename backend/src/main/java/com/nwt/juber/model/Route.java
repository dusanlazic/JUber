package com.nwt.juber.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Route {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToMany
    private List<Location> locations;
}
