package com.nwt.juber.dto.message;

import com.nwt.juber.model.RideInvitationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class InvitationStatusMessage {
    private UUID id;
    private String username;
    private String email;
    private RideInvitationStatus status;
}
