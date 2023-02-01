package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VehicleTypeDTO {
    private UUID id;
    private String name;
    private Double price;
}
