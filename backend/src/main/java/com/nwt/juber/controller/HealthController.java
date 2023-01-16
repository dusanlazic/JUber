package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.GreetRequest;
import com.nwt.juber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

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

}