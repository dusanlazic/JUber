package com.nwt.juber.service;

import com.nwt.juber.dto.PersonDTO;
import com.nwt.juber.dto.response.UserBasicInfoResponse;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.repository.PassengerRepository;
import com.nwt.juber.util.MappingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nwt.juber.util.MappingUtils.convertPersonToDTO;

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
		return new UserBasicInfoResponse(passenger.getEmail(), passenger.getFirstName(), passenger.getLastName(), passenger.getImageUrl());
	}
	
	public Passenger findByEmail(String email) {
		Optional<Passenger> possiblePassenger = passengerRepository.findByEmail(email);
		if (possiblePassenger.isEmpty()) {
			throw new UserNotFoundException("User does not exist.");
		}
		return possiblePassenger.get();
	}

	public PersonDTO getPassengersInfo(UUID passengerId) {
		Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(UserNotFoundException::new);
		return convertPersonToDTO(passenger);
	}

	public List<PersonDTO> findAll() {
		return passengerRepository.findAll().stream().map(MappingUtils::convertPersonToDTO).toList();
	}
}
