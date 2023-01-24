package com.nwt.juber.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nwt.juber.dto.request.RideReviewRequest;
import com.nwt.juber.dto.response.RideReviewResponse;
import com.nwt.juber.dto.response.RideReviewableInfoResponse;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.model.RideReview;
import com.nwt.juber.model.Role;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.RideReviewRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class RideReviewService {

	@Autowired
	RideReviewRepository rideReviewRepository;

	@Autowired
	RideService rideService;

	public void leaveReview(Passenger passenger, RideReviewRequest reviewRequest) {
		Ride rideToReview = rideService.getPastRideById(reviewRequest.getRideId());
		if (!canLeaveReview(rideToReview, passenger)) {
			// throw new RideNotReviewableException();
		}

		RideReview review = new RideReview();
		review.setReviewer(passenger);
		review.setComment(reviewRequest.getComment());
		review.setDriverRating(reviewRequest.getDriverRating());
		review.setVehicleRating(reviewRequest.getVehicleRating());
		review.setRide(rideToReview);

		rideReviewRepository.save(review);
	}

	public List<RideReviewResponse> getRideReview(UUID rideId) {
		Set<RideReview> reviews = rideReviewRepository.getRideReviewsByRideId(rideId);
		return reviews.stream().map(this::mapToResponse).toList();
	}

	public List<RideReviewResponse> getMyReviews(User user) {
		Set<RideReview> reviews = new HashSet<RideReview>();
		if (user.getRole().equals(Role.ROLE_DRIVER)) {
			reviews = rideReviewRepository.getRideReviewsByDriverId(user.getId());
		} else if (user.getRole().equals(Role.ROLE_PASSENGER)) {
			reviews = rideReviewRepository.getRideReviewsByReviewerId(user.getId());
		}
		return reviews.stream().map(this::mapToResponse).toList();
	}

	private RideReviewResponse mapToResponse(RideReview review) {
		RideReviewResponse response = new RideReviewResponse();
		response.setReviewerFullName(review.getReviewer().getFirstName() + " " + review.getReviewer().getLastName());
		response.setReviewerImageUrl(review.getReviewer().getImageUrl());
		response.setDriverRating(review.getDriverRating());
		response.setVehicleRating(review.getVehicleRating());
		response.setComment(review.getComment());
		return response;
	}

	private boolean canLeaveReview(Ride rideToReview, Passenger passenger) {
		if (rideToReview.getEndTime() == null) {
			return false;
		}
		if (!rideToReview.getPassengers().contains(passenger)) {
			return false;
		}
		LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		if (rideReviewableUntil(rideToReview).isBefore(now)) {
			return false;
		}
		return true;
	}

	private LocalDateTime rideReviewableUntil(Ride ride) {
		return ride.getEndTime().plusDays(3);
	}

	public RideReviewableInfoResponse getReviewableInfo(Passenger passenger, UUID rideId) {
		Ride rideToReview = rideService.getPastRideById(rideId);
		
		Optional<RideReview> review = rideReviewRepository.getRideReviewsByReviewerIdAndRideId(passenger.getId(), rideId);
		LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		RideReviewableInfoResponse response = new RideReviewableInfoResponse();
		response.setAlreadyReviewed(!review.isEmpty());
		response.setDeadline(rideReviewableUntil(rideToReview));
		response.setDeadlinePassed(rideReviewableUntil(rideToReview).isBefore(now));
		
		return response;
	}

}
