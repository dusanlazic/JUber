package com.nwt.juber.dto.request;

import com.nwt.juber.dto.validation.PasswordsMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordsMatch
public class PasswordResetRequest {
    @NotBlank
    @Size(min = 8, message = "Password must be longer than 8 characters.")
    private String password;

    @NotBlank
    private String passwordConfirmation;

    @NotBlank
    private String token;
}
