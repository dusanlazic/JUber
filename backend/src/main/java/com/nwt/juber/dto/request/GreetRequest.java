package com.nwt.juber.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class GreetRequest {
    @NotBlank
    @Size(min = 2, max = 60, message = "Name must be longer than 2, and shorter than 60 characters.")
    private String name;
}
