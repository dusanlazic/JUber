package com.nwt.juber.service;

import com.nwt.juber.dto.PersonDTO;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.message.InvitationStatusMessage;
import com.nwt.juber.exception.EndRideException;
import com.nwt.juber.exception.InsufficientFundsException;
import com.nwt.juber.exception.StartRideException;
import com.nwt.juber.model.*;
import com.nwt.juber.model.notification.NotificationStatus;
import com.nwt.juber.model.notification.RideCancelledNotification;
import com.nwt.juber.repository.DriverRepository;
import com.nwt.juber.repository.PassengerRepository;
import com.nwt.juber.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private DriverRepository driverRepository;


    public void startRide(UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new StartRideException("No ride with id: " + rideId));
        if (ride.getRideStatus() != RideStatus.ACCEPTED) {
            throw new StartRideException("Ride not accepted!");
        }
        ride.setRideStatus(RideStatus.IN_PROGRESS);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);
    }

    public void endRide(UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        if (ride.getRideStatus() != RideStatus.IN_PROGRESS)  {
            throw new StartRideException("Ride not in progress!");
        }
        ride.setRideStatus(RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());
        rideRepository.save(ride);
    }

    public void acceptRide(UUID rideId, Authentication authentication) throws InsufficientResourcesException {
        User user = (User) authentication.getPrincipal();
        Passenger passenger = passengerRepository.findById(user.getId()).orElseThrow();
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        BigDecimal fare = BigDecimal.valueOf(ride.getFare() / ride.getPassengers().size());

        if (passenger.getBalance().compareTo(fare) < 0) {
            // insufficent funds
            throw new InsufficientFundsException("Not enough funds!");
        }

        int passengerPosition = ride.getPassengers().indexOf(passenger);
        ride.getPassengersReady().set(passengerPosition, true);
        List<Passenger> pals = ride.getPassengers()
                .stream()
                .filter(x -> x.getId() != user.getId())
                .toList();

        if (ride.getPassengersReady().stream().allMatch(Boolean::booleanValue)) {
           subtractFundsForRide(ride);
        }

        for (Passenger pal: pals) {
            InvitationStatusMessage message = new InvitationStatusMessage();
            message.setId(passenger.getId());
            message.setStatus(RideInvitationStatus.ACCEPTED);
            messagingTemplate.convertAndSendToUser(pal.getUsername(), "/queue/ride", message);
        }
    }

    private void subtractFundsForRide(Ride ride) {
        double fare = ride.getFare() / ride.getPassengers().size();
        ride.getPassengers()
            .forEach(x -> x.setBalance(x.getBalance().subtract(BigDecimal.valueOf(fare))));
    }


    public void declineRide(UUID rideId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Passenger passenger = passengerRepository.findById(user.getId()).orElseThrow();
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        int passengerPosition = ride.getPassengers().indexOf(passenger);
        ride.getPassengersReady().set(passengerPosition, Boolean.FALSE);
        ride.setRideStatus(RideStatus.DENIED);
        // notify the rest!
        List<Passenger> pals = ride.getPassengers()
                                .stream()
                                .filter(x -> x.getId() != user.getId())
                                .toList();
        for (Passenger pal: pals) {
            InvitationStatusMessage message = new InvitationStatusMessage();
            message.setId(passenger.getId());
            message.setStatus(RideInvitationStatus.DENIED);
            messagingTemplate.convertAndSendToUser(pal.getUsername(), "/queue/ride", message);
        }
        rideRepository.save(ride);
    }

    public RideDTO getActiveRide(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Passenger> optionalPassenger = passengerRepository.findById(user.getId());
        Ride ride;
        if (optionalPassenger.isEmpty()) {
            Driver driver = driverRepository
                                            .findById(user.getId())
                                            .orElseThrow();
            ride = rideRepository.getActiveRideForDriver(driver);
        } else {
            Passenger passenger = optionalPassenger.get();
            ride = rideRepository.getActiveRideForPassenger(passenger);
        }
        // convert to dto
        if(ride == null) {
            return null;
        }
        return convertRideToDTO(ride);
    }

    private RideDTO convertRideToDTO(Ride ride) {
        RideDTO dto = new RideDTO();
        dto.setId(ride.getId());
        dto.setFare(ride.getFare());
        dto.setPlaces(ride.getPlaces());
        dto.setPassengers(ride.getPassengers().stream().map(this::convertPersonToDTO).toList());
        dto.setRideStatus(ride.getRideStatus());
        return dto;
    }

    private PersonDTO convertPersonToDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setPhoneNumber(person.getPhoneNumber());
        personDTO.setImageUrl(person.getImageUrl());
        return personDTO;
    }
}