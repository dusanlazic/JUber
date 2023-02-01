package com.nwt.juber.dto.request.ride;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
public class RideRequestDTO {
    @Valid
    @NotNull
    private RideDTO ride;

    @Valid
    private AdditionalRideRequestsDTO additionalRequests;

    @Pattern(regexp = "^$|(^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$)", message = "Time must be in HH:MM 24-hour format.")
    private String scheduleTime;

    private List<@Email String> passengerEmails;
}
