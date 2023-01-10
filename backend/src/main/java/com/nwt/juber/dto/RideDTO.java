package com.nwt.juber.dto;

import com.nwt.juber.model.Place;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class RideDTO {
    private UUID id;
    private List<Place> places;
    private Double fare;
    private List<PersonDTO> passengers;
    private PersonDTO driver;

}
