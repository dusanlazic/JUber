package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
public class RideDTO {
    private List<String> passengers;

    @Valid
    private List<PlaceDTO> places;

    @NotNull
    @Positive
    private Double fare;

    @NotNull
    @Positive
    private Integer duration;

    @NotNull
    @Positive
    private Double distance;
}
