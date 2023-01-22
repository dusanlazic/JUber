package com.nwt.juber.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.RideReviewRequest;
import com.nwt.juber.dto.response.RideReviewResponse;
import com.nwt.juber.dto.response.RideReviewableInfoResponse;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.User;
import com.nwt.juber.service.RideReviewService;

@RestController
@RequestMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideReviewController {

       @Autowired
       RideReviewService rideReviewService;

    @PostMapping("/leaveReview")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseOk leaveReview(Authentication authentication, @RequestBody RideReviewRequest review) {
       Passenger passenger = (Passenger) authentication.getPrincipal();
       rideReviewService.leaveReview(passenger, review);
        return new ResponseOk("Review successfully added");
    }

    @GetMapping("/{rideId}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER', 'ADMIN')")
    public List<RideReviewResponse> getRideReview(@PathVariable UUID rideId, Authentication authentication) {
       return rideReviewService.getRideReview(rideId);
    }

    @GetMapping("/myReviews")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public List<RideReviewResponse> getMyReviews(Authentication authentication) {
       User user = (User) authentication.getPrincipal();
       return rideReviewService.getMyReviews(user);
    }
    
    @GetMapping("/reviewableInfo/{rideId}")
    @PreAuthorize("hasAnyRole('PASSENGER')")
    public RideReviewableInfoResponse getReviewableInfo(@PathVariable UUID rideId, Authentication authentication) {
    	Passenger passenger = (Passenger) authentication.getPrincipal();
        return rideReviewService.getReviewableInfo(passenger, rideId);
    }
}
