package com.nwt.juber.dto.response;

import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
public class RideReviewResponse {

       private String reviewerFullName;
       private String reviewerImageUrl;
       private double driverRating;
    private double vehicleRating;
    private String comment;
}
