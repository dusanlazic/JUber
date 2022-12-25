package com.nwt.juber.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nwt.juber.dto.response.UserBasicInfoResponse;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.repository.PassengerRepository;

@Service
public class PassengerService {
	@Autowired
	private PassengerRepository passengerRepository;

	public UserBasicInfoResponse getBasicInfo(String email) {
		Optional<Passenger> possiblePassenger = passengerRepository.findByEmail(email);
		if (possiblePassenger.isEmpty()) {
			throw new UserNotFoundException("User does not exist.");
		}

		Passenger passenger = possiblePassenger.get();
		return new UserBasicInfoResponse(passenger.getEmail(), passenger.getFirstName(), passenger.getLastName());
	}

}
