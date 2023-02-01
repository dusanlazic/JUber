package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PlaceDTO {
    private UUID id;
    private String name;
    private String option;
    private List<RouteDTO> routes;
    private Double latitude;
    private Double longitude;
}
