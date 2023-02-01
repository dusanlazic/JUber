package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteDTO {
    private String name;
    private Double distance;
    private Double duration;
    private String coordinatesEncoded;
    private Boolean selected;
}
