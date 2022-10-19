package com.nwt.juber.controller;

import com.nwt.juber.api.ResponseOk;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthController {

    @GetMapping("/health")
    public ResponseOk health() {
        return new ResponseOk("It works.");
    }
}
