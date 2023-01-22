package com.nwt.juber.dto.response;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RideReviewableInfoResponse {

	private LocalDateTime deadline;
	private boolean alreadyReviewed;
    private boolean deadlinePassed;
}
