package com.nwt.juber.dto.request;

import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RideReviewRequest {

       @Min(1)
       @Max(5)
    private double driverRating;

       @Min(1)
       @Max(5)
    private double vehicleRating;

    @NotNull
    @Size(max = 500, message = "Comment size limit exceeded")
    private String comment;

    private UUID rideId;
}
