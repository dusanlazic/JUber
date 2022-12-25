package com.nwt.juber.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class VehicleType {

	@Id
    @Column(columnDefinition = "uuid")
	private UUID id;
	
	private String name;
	
	private Double price;
}
