package com.nwt.juber.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Route {

    @Id
    private Long id;

    @OneToMany
    private List<Location> locations;
}
