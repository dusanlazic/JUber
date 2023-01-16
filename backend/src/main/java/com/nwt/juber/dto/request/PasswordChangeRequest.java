package com.nwt.juber.dto.request;

import com.nwt.juber.dto.validation.PasswordsMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordsMatch
public class PasswordChangeRequest {
	@NotBlank
    private String currentPassword;
	
    @NotBlank
    @Size(min = 8, message = "Password must be longer than 8 characters.")
    private String newPassword;

    @NotBlank
    private String passwordConfirmation;

}
