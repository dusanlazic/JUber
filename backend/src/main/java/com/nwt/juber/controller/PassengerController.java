package com.nwt.juber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.juber.dto.response.UserBasicInfoResponse;
import com.nwt.juber.service.PassengerService;

@RestController
@RequestMapping(value = "/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerController {

	@Autowired
	private PassengerService passengerService;

	@GetMapping("/basicInfo/{email}")
	private UserBasicInfoResponse getBasicInfo(@PathVariable String email) {
		return passengerService.getBasicInfo(email);
	}
}
