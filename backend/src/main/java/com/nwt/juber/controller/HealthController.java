package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import com.nwt.juber.dto.request.GreetRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthController {

    @GetMapping("/health")
    public ResponseOk health() {
        System.out.println("Health.");
        return new ResponseOk("It works.");
    }

    @PostMapping(value = "/greet", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseOk greet(@Valid @RequestBody GreetRequest body) {
        return new ResponseOk(String.format("Hello, %s!", body.getName()));
    }

}
