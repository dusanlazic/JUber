package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileInfoResponse {
    private String firstName;
    private String lastName;
    private String city;
    private String phoneNumber;
    private String imageUrl;
    private String email;
}
