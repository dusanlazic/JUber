package com.nwt.juber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.juber.model.VehicleType;
import com.nwt.juber.service.VehicleTypeService;

@RestController
@RequestMapping(value = "/vehicleType", produces = MediaType.APPLICATION_JSON_VALUE)
public class VehicleTypeController {

	@Autowired
	private VehicleTypeService vehicleTypeService;

	@GetMapping("/findAll")
	public List<VehicleType> findAll() {
		return vehicleTypeService.findAll();
	}
}
