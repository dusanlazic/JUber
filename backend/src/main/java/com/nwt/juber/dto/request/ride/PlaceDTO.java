package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PlaceDTO {
    private UUID id;

    @NotBlank
    private String name;

    private String option;

    @Valid
    private List<RouteDTO> routes;

    @NotNull
    @DecimalMin(value = "-90", message = "Invalid coordinates.")
    @DecimalMax(value = "90", message = "Invalid coordinates.")
    private Double latitude;

    @NotNull
    @DecimalMin(value = "-180", message = "Invalid coordinates.")
    @DecimalMax(value = "180", message = "Invalid coordinates.")
    private Double longitude;
}
