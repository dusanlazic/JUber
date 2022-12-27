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

    private String name;

    private Double distance;

    private Double duration;

    private String coordinates;

    private Boolean selected;

}
