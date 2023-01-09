package com.nwt.juber.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ProfileInfoChangeResolveRequest {

    @NotNull
    @Pattern(regexp = "^APPROVED|DENIED$", message = "New status must be APPROVED or DENIED.")
    private String newStatus;
}
