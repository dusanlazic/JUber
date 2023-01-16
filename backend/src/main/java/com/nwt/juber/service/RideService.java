package com.nwt.juber.service;

import com.nwt.juber.dto.PersonDTO;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.message.InvitationStatusMessage;
import com.nwt.juber.dto.message.RideMessage;
import com.nwt.juber.dto.message.RideMessageType;
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
        List<PassengerStatus> ready = new ArrayList<>();
        ready.add(PassengerStatus.Ready);
        for (String email: rideRequest.getPassengerEmails()) {
            passengerRepository.findByEmail(email).ifPresent(pass::add);
            ready.add(PassengerStatus.Waiting);
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

    public void acceptRideDriver(Driver driver, UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        ride.setDriver(driver);
        ride.setRideStatus(RideStatus.ACCEPTED);
        rideRepository.save(ride);
        // notify everyone
        RideMessage rideMessage = new RideMessage();
        rideMessage.setRide(convertRideToDTO(ride));
        rideMessage.setType(RideMessageType.DRIVER_FOUND);
        for (Passenger pal: ride.getPassengers()) {
            messagingTemplate.convertAndSendToUser(pal.getUsername(), "/queue/ride", rideMessage);
        }
        messagingTemplate.convertAndSendToUser(driver.getUsername(), "/queue/ride", rideMessage);
    }

    public void acceptRidePassenger(Passenger passenger, UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        BigDecimal fare = BigDecimal.valueOf(ride.getFare() / ride.getPassengers().size());

        if (passenger.getBalance().compareTo(fare) < 0) {
            // insufficent funds
            throw new InsufficientFundsException("Not enough funds!");
        }

        int passengerPosition = ride.getPassengers().indexOf(passenger);
        ride.getPassengersReady().set(passengerPosition, PassengerStatus.Ready);
        List<Passenger> pals = ride.getPassengers()
                .stream()
                .filter(x -> x.getId() != passenger.getId())
                .toList();


        for (Passenger pal: pals) {
            RideMessage rideMessage = new RideMessage();
            rideMessage.setRide(convertRideToDTO(ride));
            rideMessage.setType(RideMessageType.PAL_UPDATE_STATUS);
            messagingTemplate.convertAndSendToUser(pal.getUsername(), "/queue/ride", rideMessage);
        }

        // look for a driver if everybody has accepted!
        if (ride.getPassengersReady().stream().allMatch(x -> x == PassengerStatus.Ready)) {
//            subtractFundsForRide(ride);
            ride.setRideStatus(RideStatus.WAIT);
            rideRepository.save(ride);
            assignSuitableDriver(ride);
        }
    }

    public void acceptRide(UUID rideId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Passenger> passenger = passengerRepository.findById(user.getId());
        if(passenger.isPresent()) {
            acceptRidePassenger(passenger.get(), rideId);
        } else {
            Driver driver = driverRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("No driver found!"));
            acceptRideDriver(driver, rideId);
        }
    }

    private void assignSuitableDriver(Ride ride) {
        Driver driver = findSuitableDriver(ride);
        if (driver == null) {
            throw new RuntimeException("No driver found!");
        }
        ride.setDriver(driver);
        ride.setRideStatus(RideStatus.WAIT);
        RideMessage rideMessage = new RideMessage();
        rideMessage.setRide(convertRideToDTO(ride));
        rideMessage.setType(RideMessageType.DRIVER_FOUND);
        for (var pal: ride.getPassengers()) {
            messagingTemplate.convertAndSendToUser(pal.getUsername(), "/queue/ride", rideMessage);
        }
        messagingTemplate.convertAndSendToUser(ride.getDriver().getUsername(), "/queue/ride", rideMessage);
        rideRepository.save(ride);
    }

    public Driver findSuitableDriver(Ride ride) {
        // find by conditions
        // ask the driver if he wants to start a ride
        return driverRepository.findAll().get(0);
    }

    private void subtractFundsForRide(Ride ride) {
        double fare = ride.getFare() / ride.getPassengers().size();
        ride.getPassengers()
            .forEach(x -> x.setBalance(x.getBalance().subtract(BigDecimal.valueOf(fare))));
    }

    public void declineRidePassenger(Passenger passenger, UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        int passengerPosition = ride.getPassengers().indexOf(passenger);
        ride.getPassengersReady().set(passengerPosition, PassengerStatus.Declined);
        ride.setRideStatus(RideStatus.DENIED);
        // notify the rest!
        List<Passenger> pals = ride.getPassengers()
                .stream()
                .filter(x -> x.getId() != passenger.getId())
                .toList();
        for (Passenger pal: pals) {
            RideMessage rideMessage = new RideMessage();
            rideMessage.setRide(convertRideToDTO(ride));
            rideMessage.setType(RideMessageType.PAL_UPDATE_STATUS);
            messagingTemplate.convertAndSendToUser(pal.getUsername(), "/queue/ride", rideMessage);
        }
        rideRepository.save(ride);
    }

    public void declineRideDriver(Driver driver, UUID rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new EndRideException("No ride with id: " + rideId));
        ride.setRideStatus(RideStatus.DENIED);
        rideRepository.save(ride);
        assignSuitableDriver(ride);
    }

    public void declineRide(UUID rideId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Passenger> passenger = passengerRepository.findById(user.getId());
        if(passenger.isPresent()) {
            declineRidePassenger(passenger.get(), rideId);
        } else {
            Driver driver = driverRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("No driver found!"));
            declineRideDriver(driver, rideId);
        }
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
        dto.setPassengersReady(ride.getPassengersReady());
        dto.setDriver(convertPersonToDTO(ride.getDriver()));
        return dto;
    }

    private PersonDTO convertPersonToDTO(Person person) {
        if (person == null) {
            return null;
        }
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setPhoneNumber(person.getPhoneNumber());
        personDTO.setImageUrl(person.getImageUrl());
        return personDTO;
    }
}
