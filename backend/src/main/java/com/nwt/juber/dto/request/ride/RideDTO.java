package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RideDTO {
    private List<String> passengers;
    private List<PlaceDTO> places;
    private Double fare;
    private Integer duration;
    private Double distance;
}
