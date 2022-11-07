package com.nwt.juber.dto.request;

import com.nwt.juber.dto.validation.PasswordsMatch;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@PasswordsMatch
public class DriverRegistrationRequest {
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be longer than 8 characters.")
    private String password;

    @NotBlank
    private String passwordConfirmation;

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

    @NotNull
    private Boolean babyFriendly;

    @NotNull
    private Boolean petFriendly;

    @Positive
    private Integer capacity;

}
