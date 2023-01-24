package com.nwt.juber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nwt.juber.model.VehicleType;
import com.nwt.juber.repository.VehicleTypeRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class VehicleTypeService {

	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;

	public List<VehicleType> findAll() {
		return vehicleTypeRepository.findAll();
	}

}
