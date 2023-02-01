package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class RouteDTO {
    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Double distance;

    @NotNull
    @Positive
    private Double duration;

    @NotBlank
    private String coordinatesEncoded;

    @NotNull
    private Boolean selected;
}
