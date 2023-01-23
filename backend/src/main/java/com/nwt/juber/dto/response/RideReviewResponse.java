package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideReviewResponse {
    private String reviewerFullName;
    private String reviewerImageUrl;
    private double driverRating;
    private double vehicleRating;
    private String comment;
}
