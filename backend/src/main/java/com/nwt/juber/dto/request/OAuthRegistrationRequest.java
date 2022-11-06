package com.nwt.juber.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class OAuthRegistrationRequest {
    @NotBlank
    @Size(max = 40, message = "First name cannot be longer than 40 characters.")
    private String firstName;

    @NotBlank
    @Size(max = 40, message = "Last name cannot be longer than 40 characters.")
    private String lastName;

    @NotBlank
    private String city;

    @NotBlank
    @Pattern(regexp = "^\\+[1-9][0-9]{3,14}$", message = "Phone number is not in a valid format.")
    private String phoneNumber;
}
