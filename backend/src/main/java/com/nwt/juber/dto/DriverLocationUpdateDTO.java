package com.nwt.juber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocationUpdateDTO {
    private String username;
    private Double latitude;
    private Double longitude;
}