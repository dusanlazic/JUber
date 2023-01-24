package com.nwt.juber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String city;
    private String phoneNumber;
    private String imageUrl;
    private String email;
}
