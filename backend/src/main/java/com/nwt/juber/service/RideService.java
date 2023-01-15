package com.nwt.juber.service;

import com.nwt.juber.dto.PersonDTO;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.message.InvitationStatusMessage;
import com.nwt.juber.dto.request.RideRequest;
import com.nwt.juber.exception.EndRideException;
import com.nwt.juber.exception.InsufficientFundsException;
import com.nwt.juber.exception.StartRideException;
import com.nwt.juber.model.*;
import com.nwt.juber.model.notification.NotificationStatus;
import com.nwt.juber.model.notification.RideCancelledNotification;
import com.nwt.juber.model.notification.RideInvitationNotification;
import com.nwt.juber.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;


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

    public void createRideRequest(RideRequest rideRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Passenger passenger = passengerRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("No passenger found!"));
        Ride ride = new Ride();
        List<Passenger> pass = new ArrayList<>();
        pass.add(passenger);
        List<Boolean> ready = new ArrayList<>();
        ready.add(true);
        for (String email: rideRequest.getPassengerEmails()) {
            passengerRepository.findByEmail(email).ifPresent(pass::add);
            ready.add(false);
        }
        ride.setPassengers(pass);
        ride.setPassengersReady(ready);
        ride.setRideStatus(RideStatus.WAITING_FOR_PAYMENT);
        ride.setStartTime(rideRequest.getRide().getStartTime());
        ride.setEndTime(rideRequest.getRide().getEndTime());
        ride.setPlaces(rideRequest.getRide().getPlaces());
        ride.setFare(rideRequest.getRide().getFare());
        ride.getPlaces().forEach(place -> routeRepository.saveAll(place.getRoutes()));
        ride.getPlaces().forEach(place -> place.setId(UUID.randomUUID()));
        placeRepository.saveAll(ride.getPlaces());
        rideRepository.save(ride);
        System.out.println(ride);
        for (String email: rideRequest.getPassengerEmails()) {
            sendRideInvitation(ride, email, passenger);
        }
    }

    private void sendRideInvitation(Ride ride, String email, Passenger inviter) {
        System.out.println("Sending invitation to: " + email);
        Passenger passenger = passengerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("No pal found!"));
        RideInvitationNotification notification = new RideInvitationNotification();
        notification.setId(UUID.randomUUID());
        notification.setInviter(inviter);
        notification.setInvitee(passenger);
        notification.setRide(ride);
        notification.setCreated(new Date());
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setBalance(passenger.getBalance().doubleValue());
        System.out.println(notification);
        notificationService.send(notification, passenger);
        passengerRepository.save(passenger);
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
            message.setEmail(passenger.getEmail());
            message.setUsername(passenger.getUsername());
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
            message.setUsername(passenger.getUsername());
            message.setEmail(passenger.getEmail());
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
