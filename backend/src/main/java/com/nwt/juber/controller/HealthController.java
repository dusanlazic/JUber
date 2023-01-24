package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.RideDTO;
import com.nwt.juber.dto.request.GreetRequest;
import com.nwt.juber.model.Passenger;
import com.nwt.juber.model.Ride;
import com.nwt.juber.repository.PassengerRepository;
import com.nwt.juber.repository.UserRepository;
import com.nwt.juber.service.RideService;
import com.nwt.juber.service.TaskScheduling;
import com.nwt.juber.util.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("permitAll()")
@Transactional
public class HealthController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideService rideService;

    @Autowired
    private PassengerRepository passengerRepository;

    @GetMapping("/health")
    public ResponseOk health() {
        System.out.println("Health.");
        return new ResponseOk("It works.");
    }

    @PostMapping(value = "/greet", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseOk greet(@Valid @RequestBody GreetRequest body) {
        return new ResponseOk(String.format("Hello, %s!", body.getName()));
    }

    @GetMapping("/greet-socket")
    public ResponseOk greetSocket() {
        String message = "GREET";
        this.userRepository.findAll()
                .forEach(x -> messagingTemplate.convertAndSendToUser(x.getUsername(), "/queue/ride", message));

        return new ResponseOk("ok");
    }


    @GetMapping("/greet-socket-user/{username}")
    public ResponseOk greetSocketUser(@PathVariable String username) {
        String message = "GREET";
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
        return new ResponseOk("ok");
    }


    @Autowired
    TaskScheduling taskScheduling;

    @GetMapping("schedule")
    public ResponseOk schedule() {
        System.out.println(LocalDateTime.now());
        taskScheduling.scheduling(this::doSomething, LocalDateTime.now().plusSeconds(10));
        return new ResponseOk("ok");
    }


    @GetMapping("sugavi/{email}")
    public List<RideDTO> ride(@PathVariable String email) {
        System.out.println(LocalDateTime.now());
        Passenger passenger = passengerRepository.findByEmail(email).orElseThrow();
        return passengerRepository.getFavouriteRides(passenger).stream().map(MappingUtils::convertRideToDTO).toList();
    }

    private void doSomething() {
        System.out.println(LocalDateTime.now());
        System.out.println("Doing something");
        System.out.println(userRepository.findAll().get(0).getEmail());
    }

}