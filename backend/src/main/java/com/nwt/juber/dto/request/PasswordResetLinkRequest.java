package com.nwt.juber.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class PasswordResetLinkRequest {
    @Email
    private String email;
}
